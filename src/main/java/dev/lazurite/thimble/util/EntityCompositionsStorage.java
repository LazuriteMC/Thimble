package dev.lazurite.thimble.util;

import dev.lazurite.thimble.composition.Composition;

import java.util.Set;

public interface EntityCompositionsStorage {
    void addComposition(Composition composition);
    Set<Composition> getCompositions();
}
