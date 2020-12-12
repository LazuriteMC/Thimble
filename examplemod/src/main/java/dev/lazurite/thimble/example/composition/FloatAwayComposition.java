package dev.lazurite.thimble.example.composition;

import dev.lazurite.thimble.composition.Composition;
import dev.lazurite.thimble.example.ServerInitializer;
import dev.lazurite.thimble.synchronizer.key.SynchronizedKey;
import dev.lazurite.thimble.synchronizer.key.SynchronizedKeyRegistry;
import dev.lazurite.thimble.synchronizer.type.SynchronizedTypeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

/**
 * This {@link Composition} causes any associated {@link Entity} to float upwards forever.
 * @author Ethan Johnson
 */
public class FloatAwayComposition extends Composition {
    /**
     * The identifier which allows the game to distinguish it when serialized
     * within a packet or a {@link net.minecraft.nbt.CompoundTag}.
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
     * Sets the {@link Entity} speed to zero.
     * @param player the {@link PlayerEntity} who is interacting
     * @param hand the {@link Hand} of the {@link PlayerEntity}
     */
    @Override
    public void interact(PlayerEntity player, Hand hand) {
        getSynchronizer().set(RATE, 0.0f);
    }

    /**
     * Called when the {@link Entity} is
     * removed from the {@link World}.
     */
    @Override
    public void remove() {

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
     * Gets the {@link Identifier} for this {@link Composition}.
     * @return the {@link Identifier}
     */
    @Override
    public Identifier getIdentifier() {
        return identifier;
    }
}
