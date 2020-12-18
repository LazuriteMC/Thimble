package dev.lazurite.thimble.synchronizer.packet;

import dev.lazurite.thimble.Thimble;
import dev.lazurite.thimble.composition.Composition;
import dev.lazurite.thimble.side.server.ServerInitializer;
import dev.lazurite.thimble.synchronizer.Synchronizer;
import dev.lazurite.thimble.synchronizer.key.SynchronizedKey;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.entity.Entity;
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
    public static final Identifier PACKET_ID = new Identifier(ServerInitializer.MODID, "synchronize_entry_packet");

    /**
     * Handles when the packet is received on the client.
     * @param context packet context information
     * @param buf the contents of the packet
     */
    public static void accept(PacketContext context, PacketByteBuf buf) {
        PlayerEntity player = context.getPlayer();

        /* The synchronizer's UUID */
        UUID synchronizerUuid = buf.readUuid();

        /* The actual key based on the identifier */
        SynchronizedKey key = Synchronizer.getKey(buf.readIdentifier());

        /* The entry to replace in the synchronizer */
        Synchronizer.Entry entry = new Synchronizer.Entry(key, key.getType().read(buf));

        /* Get the entity Id */
        int entityId = buf.readInt();

        context.getTaskQueue().execute(() -> {
            if (player != null) {
                World world = player.getEntityWorld();

                if (world != null) {
                    Entity entity = world.getEntityById(entityId);

                    if (entity != null) {
                        /* Find the right synchronizer and update it's entry */
                        for (Composition composition : Thimble.getStitches(entity)) {
                            Synchronizer synchronizer = composition.getSynchronizer();

                            if (synchronizer.getUuid().equals(synchronizerUuid)) {
                                synchronizer.set(entry);
                            }
                        }
                    }
                }
            }
        });
    }

    public static <T> void send(Synchronizer synchronizer, Synchronizer.Entry<T> entry, Entity entity) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        World world = entity.getEntityWorld();

        /* Write the synchronizer's UUID */
        buf.writeUuid(synchronizer.getUuid());

        /* Write the entry key */
        buf.writeIdentifier(entry.getKey().getIdentifier());

        /* Write the entry value */
        entry.getKey().getType().write(buf, entry.getValue());

        /* Write the entity Id */
        buf.writeInt(entity.getEntityId());

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
