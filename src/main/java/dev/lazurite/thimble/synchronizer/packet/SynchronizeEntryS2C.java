package dev.lazurite.thimble.synchronizer.packet;

import dev.lazurite.thimble.side.server.ServerInitializer;
import dev.lazurite.thimble.synchronizer.Synchronizer;
import dev.lazurite.thimble.synchronizer.key.SynchronizedKey;
import dev.lazurite.thimble.synchronizer.key.SynchronizedKeyRegistry;
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
 * This packet is used for synchronizing a {@link dev.lazurite.thimble.synchronizer.Synchronizer.Entry}
 * object, which was changed on the server, onto the client.
 * @author Ethan Johnson
 */
public class SynchronizeEntryS2C {
    /**
     * The packet's {@link Identifier} used for distinguishing it when received.
     */
    public static final Identifier PACKET_ID = new Identifier(ServerInitializer.MODID, "synchronize_entry_s2c");

    /**
     * Handles when the packet is received on the client.
     * @param context packet context information
     * @param buf the contents of the packet
     */
    public static void accept(PacketContext context, PacketByteBuf buf) {
        PlayerEntity player = context.getPlayer();
        Identifier identifier = buf.readIdentifier();
        int entityId = buf.readInt();
        SynchronizedKey<?> key = SynchronizedKeyRegistry.get(buf.readIdentifier());
        key.getType().read(buf);

        context.getTaskQueue().execute(() -> {

//            if (player.getEntityWorld() != null) {
//                Entity entity = player.getEntityWorld().getEntityById(entityId);
//                Composition composition = CompositionRegistry.get(compId);
//
//                if (entity != null) {
//                    if (!CompositionTracker.get(entity).contains(composition)) {
//                        CompositionTracker.attach(composition, entity);
//                    }
//                }
//            }
        });
    }

    public static <T> void send(Synchronizer synchronizer, Synchronizer.Entry<T> entry, World world) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        Entity entity = world.getEntityById(synchronizer.getEntityId());

        if (entity != null) {
            /* Write the Composition Identifier */
            buf.writeIdentifier(synchronizer.getIdentifier());

            /* Write the entity Id */
            buf.writeInt(synchronizer.getEntityId());

            /* Write the entry key */
            buf.writeIdentifier(entry.getKey().getIdentifier());

            /* Write the entry value */
            entry.getKey().getType().write(buf, entry.getValue());

            /* Send it! */
            Stream<PlayerEntity> watchingPlayers = PlayerStream.watching(world, new BlockPos(entity.getPos()));
            watchingPlayers.forEach(player -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, PACKET_ID, buf));
        }
    }

    /**
     * Registers the packet in on the client.
     */
    public static void register() {
        ClientSidePacketRegistry.INSTANCE.register(PACKET_ID, SynchronizeEntryS2C::accept);
    }
}
