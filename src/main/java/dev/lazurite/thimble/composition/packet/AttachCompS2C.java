package dev.lazurite.thimble.composition.packet;

import dev.lazurite.thimble.composition.Composition;
import dev.lazurite.thimble.composition.register.CompositionTracker;
import dev.lazurite.thimble.composition.register.CompositionRegistry;
import dev.lazurite.thimble.side.server.ServerInitializer;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.stream.Stream;

/**
 * This packet is used for facilitating {@link Composition}
 * attachment on the client after being done on the server.
 * It also handles when an {@link Entity} with a
 * {@link Composition} attached comes into range of the player.
 * @author Ethan Johnson
 */
public class AttachCompS2C {
    /**
     * The packet's {@link Identifier} used for distinguishing it when received on the client.
     */
    public static final Identifier PACKET_ID = new Identifier(ServerInitializer.MODID, "attach_comp_s2c");

    /**
     * Handles when the packet is received on the client. First checks if
     * the given {@link Entity} exists, then checks to make sure the
     * {@link Composition} is registered. Then the {@link Composition}
     * is attached.
     * @param context packet context information
     * @param buf the contents of the packet
     */
    public static void accept(PacketContext context, PacketByteBuf buf) {
        PlayerEntity player = context.getPlayer();
        Identifier compId = buf.readIdentifier();
        int entityId = buf.readInt();

        /* Attach a new composition on the client */
        context.getTaskQueue().execute(() -> {
            Composition composition = CompositionRegistry.get(compId);
            Entity entity = player.getEntityWorld().getEntityById(entityId);

            if (entity != null) {
                if (!CompositionTracker.get(entity).contains(composition)) {
                    CompositionTracker.attach(composition, entity);
                }
            }
        });
    }

    /**
     * This method is called whenever you have a {@link Composition} attached
     * to a specific {@link Entity} on the server but not the client.
     * @param composition the {@link Composition} to attach.
     * @param entity the {@link Entity} to which the {@link Composition} will be attached
     */
    public static void send(Composition composition, Entity entity) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        /* Composition Registration ID */
        buf.writeIdentifier(composition.getIdentifier());

        /* Attached Entity ID */
        buf.writeInt(entity.getEntityId());

        /* Send it! */
        Stream<PlayerEntity> watchingPlayers = PlayerStream.watching(entity.getEntityWorld(), new BlockPos(entity.getPos()));
        watchingPlayers.forEach(player -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, PACKET_ID, buf));
    }

    /**
     * Registers the packet in {@link dev.lazurite.thimble.side.client.ClientInitializer}.
     */
    public static void register() {
        ClientSidePacketRegistry.INSTANCE.register(PACKET_ID, AttachCompS2C::accept);
    }
}
