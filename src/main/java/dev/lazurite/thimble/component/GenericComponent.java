package dev.lazurite.thimble.component;

import dev.lazurite.thimble.synchronizer.Synchronizer;
import net.minecraft.entity.Entity;

public abstract class GenericComponent<T extends Entity> implements Component {
    private final Synchronizer synchronizer;
    private final Class<T> type;

    public GenericComponent(Class<T> type) {
        this.type = type;

        this.synchronizer = new Synchronizer(this);
        this.initSynchronizer();
    }

    @Override
    public Synchronizer getSynchronizer() {
        return synchronizer;
    }

    public Class<T> getType() {
        return this.type;
    }
}
