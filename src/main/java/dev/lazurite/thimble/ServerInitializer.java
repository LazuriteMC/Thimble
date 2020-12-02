package dev.lazurite.thimble;

import dev.lazurite.thimble.component.Components;
import dev.lazurite.thimble.synchronizer.Synchronizer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class ServerInitializer implements ModInitializer {
	public static final String MODID = "thimble";

	@Override
	public void onInitialize() {
		ServerTickEvents.START_WORLD_TICK.register(Synchronizer::tick);
		ServerTickEvents.START_WORLD_TICK.register(Components::tick);
	}
}
