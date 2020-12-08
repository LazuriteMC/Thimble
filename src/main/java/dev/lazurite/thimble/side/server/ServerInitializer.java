package dev.lazurite.thimble.side.server;

import dev.lazurite.thimble.synchronizer.register.SynchronizerRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class ServerInitializer implements ModInitializer {
	public static final String MODID = "thimble";

	@Override
	public void onInitialize() {
		ServerTickEvents.START_WORLD_TICK.register(world -> SynchronizerRegistry.tick());
	}
}
