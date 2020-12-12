package dev.lazurite.thimble.composition.exception;

import dev.lazurite.thimble.composition.Composition;

/**
 * This exception is typically used if the user tries to stitch a {@link Composition}
 * which isn't registered in {@link dev.lazurite.thimble.Thimble}.
 * @author Ethan Johnson
 */
public class CompositionRegistryException extends RuntimeException {
    public CompositionRegistryException(String errorMessage) {
        super(errorMessage);
    }
}
