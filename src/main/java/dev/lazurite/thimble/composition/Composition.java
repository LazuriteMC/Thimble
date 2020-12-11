package dev.lazurite.thimble.composition;

import dev.lazurite.thimble.synchronizer.Synchronizer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

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
    public abstract Identifier getIdentifier();

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Composition) {
            return ((Composition) obj).getIdentifier().equals(getIdentifier());
        }

        return false;
    }
}
