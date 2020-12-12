package dev.lazurite.thimble.synchronizer.packet;

import dev.lazurite.thimble.Thimble;
import dev.lazurite.thimble.side.server.ServerInitializer;
import dev.lazurite.thimble.synchronizer.Synchronizer;
import dev.lazurite.thimble.synchronizer.key.SynchronizedKeyRegistry;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.UUID;

/**
 * This packet is used for synchronizing a {@link Synchronizer.Entry}
 * between the client and the server.
 * @author Ethan Johnson
 */
public class SynchronizeEntryPacket {
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

        /* The synchronizer's UUID */
        UUID synchronizerUuid = buf.readUuid();

        /* The key's identifier */
        Identifier keyIdentifier = buf.readIdentifier();

        context.getTaskQueue().execute(() -> {
            Thimble.getAllStitches().forEach(composition -> {
                Synchronizer synchronizer = composition.getSynchronizer();

                if (synchronizer.getUuid().equals(synchronizerUuid)) {
                    synchronizer.set(SynchronizedKeyRegistry.get(keyIdentifier), buf);
                }
            });
        });
    }

    public static <T> void send(Synchronizer synchronizer, Synchronizer.Entry<T> entry, World world) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        /* Write the synchronizer's UUID */
        buf.writeUuid(synchronizer.getUuid());

        /* Write the entry key */
        buf.writeIdentifier(entry.getKey().getIdentifier());

        /* Write the entry value */
        entry.getKey().getType().write(buf, entry.getValue());

        /* Send it! */
        if (world.isClient()) {
            ClientSidePacketRegistry.INSTANCE.sendToServer(PACKET_ID, buf);
        } else {
            PlayerStream.world(world).forEach(
                    player -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, PACKET_ID, buf)
            );
        }
    }
}
