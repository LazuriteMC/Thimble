package dev.lazurite.thimble.composition.packet;

import dev.lazurite.thimble.Thimble;
import dev.lazurite.thimble.composition.Composition;
import dev.lazurite.thimble.composition.CompositionFactory;
import dev.lazurite.thimble.side.server.ServerInitializer;
import dev.lazurite.thimble.synchronizer.Synchronizer;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;
import java.util.stream.Stream;

/**
 * This packet is used for facilitating {@link Composition}
 * stitching on the client after being done on the server.
 * It also handles when an {@link Entity} with a
 * {@link Composition} stitched comes into range of the player.
 * @author Ethan Johnson
 */
public class StitchCompositionS2C {
    /**
     * The packet's {@link Identifier} used for distinguishing it when received.
     */
    public static final Identifier PACKET_ID = new Identifier(ServerInitializer.MODID, "stitch_composition_s2c");

    /**
     * Handles when the packet is received on the client. First checks if
     * the given {@link Entity} exists, then checks to make sure the
     * {@link Composition} is registered. Then the {@link Composition}
     * is stitched in.
     * @param context packet context information
     * @param buf the contents of the packet
     */
    public static void accept(PacketContext context, PacketByteBuf buf) {
        PlayerEntity player = context.getPlayer();
        Identifier compId = buf.readIdentifier();
        UUID synchronizerId = buf.readUuid();
        int entityId = buf.readInt();

        /* Stitch a new composition on the client */
        context.getTaskQueue().execute(() -> {
            if (player != null) {
                if (player.getEntityWorld() != null) {
                    Entity entity = player.getEntityWorld().getEntityById(entityId);
                    CompositionFactory factory = Thimble.getRegistered(compId);

                    if (entity != null && factory != null) {
                        Thimble.stitch(factory, entity, new Synchronizer(synchronizerId));
                    }
                }
            }
        });
    }

    /**
     * This method is called whenever you have a {@link Composition} stitched
     * to a specific {@link Entity} on the server but the client may or may not have it.
     * @param composition the {@link Composition} to stitch
     * @param entity the {@link Entity} to which the {@link Composition} will be stitched
     */
    public static void send(Composition composition, Entity entity) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        /* Composition Registration ID */
        buf.writeIdentifier(composition.getIdentifier());

        /* Synchronizer ID */
        buf.writeUuid(composition.getSynchronizer().getUuid());

        /* Stitched Entity ID */
        buf.writeInt(entity.getEntityId());

        /* Send it! */
        Stream<PlayerEntity> watchingPlayers = PlayerStream.watching(entity.getEntityWorld(), new BlockPos(entity.getPos()));
        watchingPlayers.forEach(player -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, PACKET_ID, buf));
    }
}
