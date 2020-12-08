package dev.lazurite.thimble.synchronizer.packet;

import dev.lazurite.thimble.side.server.ServerInitializer;
import dev.lazurite.thimble.synchronizer.Synchronizer;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class SynchronizerEntryC2S {
    public static final Identifier PACKET_ID = new Identifier(ServerInitializer.MODID, "synchronizer_entry_c2s");

    public static void accept(PacketContext context, PacketByteBuf buf) {
        UUID synchronizerUUID = buf.readUuid();
        UUID keyID = buf.readUuid();
        UUID typeID = buf.readUuid();

        context.getTaskQueue().execute(() -> {
//            Synchronizer synchronizer = ServerInitializer.synchronizerRegistry.get(uuid);
        });
    }

    public static <T> void send(UUID uuid, Synchronizer.Entry<T> entry) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeUuid(uuid); // synchronizer uuid
        buf.writeUuid(entry.getKey().getUuid()); // key uuid
        entry.getKey().getType().write(buf, entry.getValue()); // value
//        buf.writeUuid(SynchronizedTypeRegistry.get)
        ClientSidePacketRegistry.INSTANCE.sendToServer(PACKET_ID, buf);
    }

    /**
     * Registers the packet in {@link ServerInitializer}.
     */
    public static void register() {
        ServerSidePacketRegistry.INSTANCE.register(PACKET_ID, SynchronizerEntryC2S::accept);
    }
}
