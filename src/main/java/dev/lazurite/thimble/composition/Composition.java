package dev.lazurite.thimble.composition;

import dev.lazurite.thimble.synchronizer.Synchronizer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

/**
 * The main class that you want to extend from. This class allows you to define
 * custom behavior for any {@link Entity} with a {@link Composition} stitched to it.
 * @author Ethan Johnson
 */
public abstract class Composition {
    /**
     * The {@link Synchronizer} used for syncing values
     * between the client and the server.
     */
    private final Synchronizer synchronizer;

    /**
     * The default constructor. It creates and initializes
     * the {@link Synchronizer} object.
     */
    public Composition(Synchronizer synchronizer) {
        this.synchronizer = synchronizer;
        this.initSynchronizer();
    }

    /**
     * This is where you can define your ticking custom
     * behavior. An override of this method is
     * required when you extend this class.
     * @param entity the entity which this {@link Composition} is attached to
     */
    public abstract void onTick(Entity entity);

    /**
     * Called whenever a {@link PlayerEntity} interacts with the
     * {@link Entity} that this {@link Composition} is stitched into.
     * @param entity the {@link Entity} who has the {@link Composition} stitched
     * @param player the {@link PlayerEntity} who is interacting
     * @param hand the {@link Hand} of the {@link PlayerEntity}
     * @return whether or not the player should swing their hand
     */
    public abstract boolean onInteract(Entity entity, PlayerEntity player, Hand hand);

    /**
     * Called whenever the {@link Entity} is removed
     * from the {@link net.minecraft.world.World}.
     * @param entity the {@link Entity} who has the {@link Composition} stitched
     */
    public abstract void onRemove(Entity entity);

    /**
     * Initializes the {@link Synchronizer}. This is where
     * you would call {@link Synchronizer#track}.
     */
    public abstract void initSynchronizer();

    /**
     * @return the {@link Identifier} object that you define
     * in your {@link Composition} implementation.
     */
    public abstract Identifier getIdentifier();

    /**
     * @return the {@link Synchronizer} object
     */
    public Synchronizer getSynchronizer() {
        return this.synchronizer;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Composition) {
            return ((Composition) obj).getIdentifier().equals(getIdentifier());
        }

        return false;
    }
}
