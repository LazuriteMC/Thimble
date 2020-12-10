package dev.lazurite.thimble.side.server;

import dev.lazurite.thimble.composition.packet.AttachComposition;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;

public class ServerInitializer implements ModInitializer {
	public static final String MODID = "thimble";

	@Override
	public void onInitialize() {
		ServerSidePacketRegistry.INSTANCE.register(AttachComposition.PACKET_ID, AttachComposition::accept);
	}
}
