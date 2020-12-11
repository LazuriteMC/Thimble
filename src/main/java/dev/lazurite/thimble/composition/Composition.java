package dev.lazurite.thimble.composition;

import dev.lazurite.thimble.synchronizer.Synchronizer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

/**
 * The main class that you want to extend from. This class allows you
 * to define custom behavior for any {@link Entity} attached with
 * a {@link Composition} attached.
 * @author Ethan Johnson
 */
public abstract class Composition {
    private final Synchronizer synchronizer;

    /**
     * The default constructor. It creates and initializes
     * the {@link Synchronizer} object.
     */
    public Composition() {
        this.synchronizer = new Synchronizer();
        this.initSynchronizer();
    }

    /**
     * @return the {@link Synchronizer} object
     */
    public Synchronizer getSynchronizer() {
        return this.synchronizer;
    }

    /**
     * This is where you can define your custom
     * behavior. An override of this method is
     * required when you extend this class.
     * @param entity the entity which this {@link Composition} is attached to
     */
    public abstract void tick(Entity entity);

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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Composition) {
            return ((Composition) obj).getIdentifier().equals(getIdentifier());
        }

        return false;
    }
}
