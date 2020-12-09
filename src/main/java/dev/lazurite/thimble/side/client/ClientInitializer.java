package dev.lazurite.thimble.side.client;

import dev.lazurite.thimble.composition.packet.AttachCompS2C;
import net.fabricmc.api.ClientModInitializer;

public class ClientInitializer implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        AttachCompS2C.register();
    }
}
