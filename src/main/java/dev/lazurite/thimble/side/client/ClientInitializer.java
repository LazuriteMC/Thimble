package dev.lazurite.thimble.side.client;

import dev.lazurite.thimble.composition.packet.StitchCompositionS2C;
import net.fabricmc.api.ClientModInitializer;

/**
 * Really doesn't do much.
 * @author Ethan Johnson
 */
public class ClientInitializer implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        StitchCompositionS2C.register();
    }

}
