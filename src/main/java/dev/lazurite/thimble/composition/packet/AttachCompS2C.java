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

public class AttachCompS2C {
    public static final Identifier PACKET_ID = new Identifier(ServerInitializer.MODID, "attach_comp_s2c");

    public static void accept(PacketContext context, PacketByteBuf buf) {
        World world = context.getPlayer().getEntityWorld();
        int compID = buf.readInt();
        int entityID = buf.readInt();

        /* Attach a new composition on the client */
        context.getTaskQueue().execute(() -> {
            Composition composition = CompositionRegistry.get(compID);
            Entity entity = world.getEntityById(entityID);

            if (!CompositionTracker.get(entity).contains(composition)) {
                CompositionTracker.attach(composition, entity);
            }
        });
    }

    public static void send(Composition composition, Entity entity) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        /* Composition Registration ID */
        buf.writeInt(CompositionRegistry.get(composition));

        /* Attached Entity ID */
        buf.writeInt(entity.getEntityId());

        Stream<PlayerEntity> watchingPlayers = PlayerStream.watching(entity.getEntityWorld(), new BlockPos(entity.getPos()));
        watchingPlayers.forEach(player -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, PACKET_ID, buf));
    }

    /**
     * Registers the packet in {@link ServerInitializer}.
     */
    public static void register() {
        ClientSidePacketRegistry.INSTANCE.register(PACKET_ID, AttachCompS2C::accept);
    }
}
