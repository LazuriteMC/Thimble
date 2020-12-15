package dev.lazurite.thimble.side.server;

import dev.lazurite.thimble.synchronizer.packet.SynchronizeEntryPacket;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;

/**
 * Doesn't do much.
 * @author Ethan Johnson
 */
public class ServerInitializer implements ModInitializer {
	public static final String MODID = "thimble";

	@Override
	public void onInitialize() {
		ServerSidePacketRegistry.INSTANCE.register(SynchronizeEntryPacket.PACKET_ID, SynchronizeEntryPacket::accept);
	}
}
