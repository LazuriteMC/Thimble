package dev.lazurite.thimble.composition.packet;

import dev.lazurite.thimble.composition.Composition;
import dev.lazurite.thimble.side.server.ServerInitializer;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class RegisterUniqueCompS2C {
    public static final Identifier PACKET_ID = new Identifier(ServerInitializer.MODID, "register_unique_comp_s2c");

    public static void accept(PacketContext context, PacketByteBuf buf) {
        int entityId = buf.readInt();

        context.getTaskQueue().execute(() -> {

        });
    }

    public static <T extends Entity> void send(Composition component) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

//        buf.writeInt(component.getOwner().getEntityId());

        ClientSidePacketRegistry.INSTANCE.sendToServer(PACKET_ID, buf);
    }

    /**
     * Registers the packet in {@link ServerInitializer}.
     */
    public static void register() {
        ServerSidePacketRegistry.INSTANCE.register(PACKET_ID, RegisterUniqueCompS2C::accept);
    }
}
