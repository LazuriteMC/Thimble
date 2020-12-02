package dev.lazurite.thimble.component;

import dev.lazurite.thimble.synchronizer.Synchronizer;

public abstract class Component<T> {
    private final Synchronizer synchronizer;
    private boolean destroyed = false;
    private final T owner;

    public Component(T owner) {
        this.owner = owner;
        this.synchronizer = new Synchronizer(this);
        this.initSynchronizer();
    }

    public abstract void tick();

    public abstract void initSynchronizer();

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
}
