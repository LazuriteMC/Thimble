package dev.lazurite.thimble.synchronizer.key;

import dev.lazurite.thimble.composition.Composition;
import dev.lazurite.thimble.synchronizer.type.SynchronizedType;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

public class SynchronizedKey<T> {
    private BiConsumer<Composition, T> consumer;
    private final SynchronizedType<T> type;
    private final Identifier identifier;
    private final T fallback;

    public SynchronizedKey(Identifier identifier, SynchronizedType<T> type, T fallback) {
        this.identifier = identifier;
        this.type = type;
        this.fallback = fallback;
    }

    public SynchronizedKey(Identifier identifier, SynchronizedType<T> type, T fallback, BiConsumer<Composition, T> consumer) {
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

    public BiConsumer<Composition, T> getConsumer() {
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
