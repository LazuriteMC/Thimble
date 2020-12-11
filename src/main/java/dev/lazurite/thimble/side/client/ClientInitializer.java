package dev.lazurite.thimble.side.client;

import dev.lazurite.thimble.composition.packet.AttachCompositionS2C;
import net.fabricmc.api.ClientModInitializer;

/**
 * Really doesn't do much.
 * @author Ethan Johnson
 */
public class ClientInitializer implements ClientModInitializer {
    /**
     * Register the {@link AttachCompositionS2C} packet.
     */
    @Override
    public void onInitializeClient() {
        AttachCompositionS2C.register();
    }
}
