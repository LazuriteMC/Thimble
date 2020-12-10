package dev.lazurite.thimble.side.client;

import dev.lazurite.thimble.composition.packet.AttachComposition;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;

public class ClientInitializer implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientSidePacketRegistry.INSTANCE.register(AttachComposition.PACKET_ID, AttachComposition::accept);
    }
}
