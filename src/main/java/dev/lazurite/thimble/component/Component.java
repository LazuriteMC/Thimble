package dev.lazurite.thimble.component;

import dev.lazurite.thimble.synchronizer.Synchronizer;
import net.minecraft.entity.Entity;

public abstract class Component<T extends Entity> {
    private final Synchronizer synchronizer;
    private boolean destroyed = false;
    private final T owner;

    public Component(T owner) {
        this.owner = owner;
        this.synchronizer = new Synchronizer(this);
        this.initSynchronizer();
    }

    public abstract void tick();

    public Synchronizer getSynchronizer() {
        return synchronizer;
    }

    public T getOwner() {
        return owner;
    }

    public void destroy() {
        destroyed = true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public abstract void initSynchronizer();
}
