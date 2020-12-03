package dev.lazurite.thimble.server;

import dev.lazurite.thimble.registry.ComponentRegistry;
import dev.lazurite.thimble.registry.SynchronizerRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class ServerInitializer implements ModInitializer {
	public static final String MODID = "thimble";

	public static final ComponentRegistry componentRegistry = new ComponentRegistry();
	public static final SynchronizerRegistry synchronizerRegistry = new SynchronizerRegistry();

	@Override
	public void onInitialize() {
		ServerTickEvents.START_WORLD_TICK.register(world -> componentRegistry.tick());
		ServerTickEvents.START_WORLD_TICK.register(world -> synchronizerRegistry.tick());
	}
}
