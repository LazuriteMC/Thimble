package dev.lazurite.thimble.composition;

import dev.lazurite.thimble.synchronizer.Synchronizer;
import net.minecraft.entity.Entity;

public abstract class Composition {
    private final Synchronizer synchronizer;

    public Composition() {
        this.synchronizer = new Synchronizer();
        this.initSynchronizer();
    }

    public Synchronizer getSynchronizer() {
        return this.synchronizer;
    }

    public abstract void tick(Entity entity);
    public abstract void initSynchronizer();
}
