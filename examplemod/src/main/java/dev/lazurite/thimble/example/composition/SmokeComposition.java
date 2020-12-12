package dev.lazurite.thimble.example.composition;

import dev.lazurite.thimble.composition.Composition;
import dev.lazurite.thimble.example.ServerInitializer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

/**
 * This {@link Composition} causes any associated {@link Entity} to emit smoke particles.
 * @author Ethan Johnson
 */
public class SmokeComposition extends Composition {
    /**
     * The identifier which allows the game to distinguish it when serialized
     * within a packet or a {@link net.minecraft.nbt.CompoundTag}.
     */
    public static final Identifier identifier = new Identifier(ServerInitializer.MODID, "smoke");

    /**
     * Default constructor, necessary in order to register the {@link Composition}.
     */
    public SmokeComposition(int entityId) {
        super(entityId);
    }

    /**
     * The main spot where you'll define your custom behavior. All this
     * does is emit smoke particles on the client-side of the game.
     * @param entity the {@link Entity} with this {@link Composition} attached
     */
    @Override
    public void tick(Entity entity) {
        World world = entity.getEntityWorld();

        /* Only do this sort of thing on the client */
        if (world.isClient()) {
            ClientWorld clientWorld = (ClientWorld) world;
            clientWorld.addParticle(ParticleTypes.LARGE_SMOKE, true, entity.getX(), entity.getY(), entity.getZ(), 0, 0, 0);
        }
    }

    /**
     * This method doesn't contain anything because this {@link Composition}
     * doesn't have any synchronized values to track.
     */
    @Override
    public void initSynchronizer() {

    }

    @Override
    public Identifier getIdentifier() {
        return identifier;
    }
}
