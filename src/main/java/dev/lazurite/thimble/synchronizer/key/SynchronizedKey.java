package dev.lazurite.thimble.synchronizer.key;

import dev.lazurite.thimble.synchronizer.type.SynchronizedType;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

/**
 * A {@link SynchronizedKey} object represents the name of a synchronized value to
 * be synced across the network. It doesn't actually contain the value, but serves
 * as a way for the {@link dev.lazurite.thimble.synchronizer.Synchronizer} to reference it.
 * @param <T> the type of the key
 */
public class SynchronizedKey<T> {
    private BiConsumer<Entity, T> consumer;
    private final SynchronizedType<T> type;
    private final Identifier identifier;
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
     * to a {@link BiConsumer} object. The purpose of the {@link BiConsumer} is to allow
     * you to execute custom code whenever the synchronized value is changed.
     * @param identifier the {@link Identifier} used for recognizing the key when read from disk or a packet
     * @param type the type of the key
     * @param fallback the fallback value of the key in case the value is not initialized
     * @param consumer the {@link BiConsumer} which is executed any time the value is changed
     */
    public SynchronizedKey(Identifier identifier, SynchronizedType<T> type, T fallback, BiConsumer<Entity, T> consumer) {
        this(identifier, type, fallback);
        this.consumer = consumer;
    }

    public Identifier getIdentifier() {
        return this.identifier;
    }

    public SynchronizedType<T> getType() {
        return this.type;
    }

    public T getFallback() {
        return this.fallback;
    }

    public BiConsumer<Entity, T> getConsumer() {
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
