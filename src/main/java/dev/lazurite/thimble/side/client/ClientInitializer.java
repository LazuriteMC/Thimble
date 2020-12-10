package dev.lazurite.thimble.side.client;

import dev.lazurite.thimble.composition.packet.AttachCompositionS2C;
import net.fabricmc.api.ClientModInitializer;

public class ClientInitializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AttachCompositionS2C.register();
    }
}
