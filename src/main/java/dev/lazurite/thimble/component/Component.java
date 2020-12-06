package dev.lazurite.thimble.component;

import dev.lazurite.thimble.synchronizer.Synchronizer;

public interface Component {
    void tick();
    Synchronizer getSynchronizer();
    void initSynchronizer();
}
