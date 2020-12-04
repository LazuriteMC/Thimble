package dev.lazurite.thimble.side.client;

import dev.lazurite.thimble.registry.SynchronizerRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class ClientInitializer implements ClientModInitializer {
    public static final SynchronizerRegistry synchronizerRegistry = new SynchronizerRegistry();

    @Override
    public void onInitializeClient() {
        ClientTickEvents.START_WORLD_TICK.register(world -> synchronizerRegistry.tick());
    }
}
