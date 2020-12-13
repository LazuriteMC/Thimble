package dev.lazurite.thimble.util;

import dev.lazurite.thimble.composition.Composition;

import java.util.List;

public interface EntityCompositionsStorage {
    void addComposition(Composition composition);
    List<Composition> getCompositions();
}
