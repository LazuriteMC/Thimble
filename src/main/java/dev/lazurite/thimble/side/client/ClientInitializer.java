package dev.lazurite.thimble.side.client;

import dev.lazurite.thimble.composition.packet.StitchCompositionS2C;
import dev.lazurite.thimble.synchronizer.packet.SynchronizeEntryPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;

/**
 * Really doesn't do much.
 * @author Ethan Johnson
 */
public class ClientInitializer implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientSidePacketRegistry.INSTANCE.register(StitchCompositionS2C.PACKET_ID, StitchCompositionS2C::accept);
        ClientSidePacketRegistry.INSTANCE.register(SynchronizeEntryPacket.PACKET_ID, SynchronizeEntryPacket::accept);
    }
}
