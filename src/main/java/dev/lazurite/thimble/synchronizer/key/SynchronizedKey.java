package dev.lazurite.thimble.synchronizer.key;

import dev.lazurite.thimble.synchronizer.type.SynchronizedType;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

/**
 * A {@link SynchronizedKey} object represents the name of a synchronized value to
 * be synced across the network. It doesn't actually contain the value, but serves
 * as a way for the {@link dev.lazurite.thimble.synchronizer.Synchronizer} to reference it.
 * @param <T> the type of the key
 */
public class SynchronizedKey<T> {
    /** The type of the key. */
    private final SynchronizedType<T> type;

    /** The {@link Identifier} for the key. */
    private final Identifier identifier;

    /** The custom {@link Consumer} object for the key. */
    private Consumer<T> consumer;

    /** The fallback value for the key. */
    private final T fallback;

    /**
     * Basic constructor that passes in all the necessary key information.
     * @param identifier the {@link Identifier} used for recognizing the key when read from disk or a packet
     * @param type the type of the key
     * @param fallback the fallback value of the key in case the value is not initialized
     */
    public SynchronizedKey(Identifier identifier, SynchronizedType<T> type, T fallback) {
        this.identifier = identifier;
        this.type = type;
        this.fallback = fallback;
    }

    /**
     * Another basic constructor that passes all the necessary information in addition
     * to a {@link Consumer} object. The purpose of the {@link Consumer} is to allow
     * you to execute custom code whenever the synchronized value is changed.
     * @param identifier the {@link Identifier} used for recognizing the key when read from disk or a packet
     * @param type the type of the key
     * @param fallback the fallback value of the key in case the value is not initialized
     * @param consumer the {@link Consumer} which is executed any time the value is changed
     */
    public SynchronizedKey(Identifier identifier, SynchronizedType<T> type, T fallback, Consumer<T> consumer) {
        this(identifier, type, fallback);
        this.consumer = consumer;
    }

    /**
     * Mainly used for identifying the key
     * when serialized over the network or
     * inside of a {@link net.minecraft.nbt.CompoundTag}.
     * @return the {@link Identifier} used for identification
     */
    public Identifier getIdentifier() {
        return this.identifier;
    }

    /**
     * The type of the key which is used to generalize
     * the serialization/deserialization process.
     * @return the {@link SynchronizedType} of the key
     */
    public SynchronizedType<T> getType() {
        return this.type;
    }

    /**
     * The fallback value is used in situations
     * where an actual tracked value isn't available
     * or relevant.
     * @return the fallback value
     */
    public T getFallback() {
        return this.fallback;
    }

    /**
     * The consumer is used for executing custom
     * code whenever a value is set within the
     * {@link dev.lazurite.thimble.synchronizer.Synchronizer}.
     * @return the {@link Consumer} containing custom code
     */
    public Consumer<T> getConsumer() {
        return this.consumer;
    }

    @Override
    public String toString() {
        return this.identifier.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof SynchronizedKey) {
            return ((SynchronizedKey<?>) object).getIdentifier().equals(getIdentifier());
        }

        return false;
    }
}
