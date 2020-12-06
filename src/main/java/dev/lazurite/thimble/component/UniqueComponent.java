package dev.lazurite.thimble.component;

import dev.lazurite.thimble.synchronizer.Synchronizer;
import net.minecraft.entity.Entity;

public abstract class UniqueComponent<T extends Entity> implements Component {
    private final Synchronizer synchronizer;
    private final T owner;

    public UniqueComponent(T owner) {
        this.owner = owner;

        this.synchronizer = new Synchronizer(this);
        this.initSynchronizer();
    }

    @Override
    public Synchronizer getSynchronizer() {
        return this.synchronizer;
    }

    public T getOwner() {
        return this.owner;
    }
}
