package dev.lazurite.thimble.composition;

import dev.lazurite.thimble.synchronizer.Synchronizer;
import net.minecraft.entity.Entity;

public abstract class Composition {
    private Synchronizer synchronizer;

    public Composition() {
        synchronizer = new Synchronizer(this);
        this.initSynchronizer();
    }

    public abstract void tick(Entity entity);
    public abstract void initSynchronizer();

    public Synchronizer getSynchronizer() {
        return this.synchronizer;
    }
}
