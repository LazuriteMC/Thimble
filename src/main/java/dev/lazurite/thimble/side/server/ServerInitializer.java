package dev.lazurite.thimble.side.server;

import dev.lazurite.thimble.registry.SynchronizerRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class ServerInitializer implements ModInitializer {
	public static final String MODID = "thimble";

	public static final SynchronizerRegistry synchronizerRegistry = new SynchronizerRegistry();

	@Override
	public void onInitialize() {
		ServerTickEvents.START_WORLD_TICK.register(world -> synchronizerRegistry.tick());
	}
}
