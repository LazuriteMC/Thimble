package dev.lazurite.thimble.composition;

import net.minecraft.entity.Entity;

public abstract class Composition {
    public Composition() {

    }

    public abstract void tick(Entity entity);
}
