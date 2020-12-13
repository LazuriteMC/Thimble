package dev.lazurite.thimble.exception;

import dev.lazurite.thimble.synchronizer.key.SynchronizedKey;

/**
 * This exception is typically used if the user tries to use a {@link SynchronizedKey}
 * that isn't registered {@link dev.lazurite.thimble.synchronizer.Synchronizer}.
 * @author Ethan Johnson
 */
public class SynchronizedKeyException extends RuntimeException {
    public SynchronizedKeyException(String errorMessage) {
        super(errorMessage);
    }
}
