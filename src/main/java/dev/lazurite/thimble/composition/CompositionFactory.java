package dev.lazurite.thimble.composition;

import dev.lazurite.thimble.synchronizer.Synchronizer;

public interface CompositionFactory {
    Composition create(Synchronizer synchronizer);
}
