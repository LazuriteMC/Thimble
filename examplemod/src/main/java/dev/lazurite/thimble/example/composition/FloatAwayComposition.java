package dev.lazurite.thimble.example.composition;

import dev.lazurite.thimble.composition.Composition;
import dev.lazurite.thimble.example.ServerInitializer;
import dev.lazurite.thimble.synchronizer.key.SynchronizedKey;
import dev.lazurite.thimble.synchronizer.key.SynchronizedKeyRegistry;
import dev.lazurite.thimble.synchronizer.type.SynchronizedTypeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

/**
 * This {@link Composition} causes any associated {@link Entity} to float upwards forever.
 * @author Ethan Johnson
 */
public class FloatAwayComposition extends Composition {
    /**
     * The identifier, necessary for communicating {@link Composition} info
     * over the network and saving to {@link net.minecraft.nbt.CompoundTag} objects.
     */
    public static final Identifier identifier = new Identifier(ServerInitializer.MODID, "float_away");

    /**
     * A synchronized value, representing the rate at which an associated
     * {@link Entity} would float upwards.
     */
    public static final SynchronizedKey<Float> RATE = SynchronizedKeyRegistry.register(new Identifier(ServerInitializer.MODID, "rate"), SynchronizedTypeRegistry.FLOAT, 0.05f);

    /**
     * Default constructor, necessary in order to register the {@link Composition}.
     */
    public FloatAwayComposition() {

    }

    /**
     * A constructor which allows you to set the rate on creation.
     * @param rate the rate of upwards movement
     */
    public FloatAwayComposition(float rate) {
        getSynchronizer().set(RATE, rate);
    }

    /**
     * The main spot where you'll define your custom behavior. All this
     * does is set the upwards velocity of the {@link Entity} to the rate.
     * @param entity the {@link Entity} with this {@link Composition} attached
     */
    @Override
    public void tick(Entity entity) {
        World world = entity.getEntityWorld();

        /* Only do this sort of thing on the server */
        if (!world.isClient()) {
            entity.setVelocity(0, getSynchronizer().get(RATE), 0);
        }
    }

    /**
     * This is where the rate value is set up to be tracked by
     * the {@link dev.lazurite.thimble.synchronizer.Synchronizer}.
     */
    @Override
    public void initSynchronizer() {
        getSynchronizer().track(RATE);
    }

    /**
     * @return the {@link Identifier} defined at the top of the class.
     */
    @Override
    public Identifier getIdentifier() {
        return identifier;
    }
}
