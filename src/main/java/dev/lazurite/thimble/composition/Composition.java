package dev.lazurite.thimble.composition;

import dev.lazurite.thimble.synchronizer.Synchronizer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public abstract class Composition {
    private final Synchronizer synchronizer;
    private boolean fresh = true;

    public Composition() {
        this.synchronizer = new Synchronizer();
        this.initSynchronizer();
    }

    public Synchronizer getSynchronizer() {
        return this.synchronizer;
    }

    public abstract void tick(Entity entity);
    public abstract void initSynchronizer();
    public abstract Identifier getIdentifier();

    /**
     * If the {@link Composition} is fresh, it
     * means that it was newly created and doesn't
     * exist on both sides (server and client) of
     * the game.
     * @return whether or not the {@link Composition} is fresh
     */
    public boolean isFresh() {
        return this.fresh;
    }

    /**
     * Sets the {@link Composition} to be stale. This lets
     * Thimble know that this {@link Composition} has already
     * been synced between the client and the server.
     */
    public void setStale() {
        this.fresh = false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Composition) {
            return ((Composition) obj).getClass().equals(getClass());
        }

        return false;
    }
}
