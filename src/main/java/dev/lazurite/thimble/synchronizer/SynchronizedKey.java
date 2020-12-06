package dev.lazurite.thimble.synchronizer;

import dev.lazurite.thimble.component.GenericComponent;

import java.util.UUID;
import java.util.function.BiConsumer;

public class SynchronizedKey<T> {
    private BiConsumer<GenericComponent<?>, T> consumer;
    private final SynchronizedType<T> type;
    private final T fallback;
    private UUID uuid;

    public SynchronizedKey(SynchronizedType<T> type, T fallback) {
        this.type = type;
        this.fallback = fallback;
        this.uuid = UUID.randomUUID();
    }

    public SynchronizedKey(SynchronizedType<T> type, T fallback, BiConsumer<GenericComponent<?>, T> consumer) {
        this(type, fallback);
        this.consumer = consumer;
    }

    public SynchronizedType<T> getType() {
        return this.type;
    }

    public T getFallback() {
        return this.fallback;
    }

    public BiConsumer<GenericComponent<?>, T> getConsumer() {
        return this.consumer;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof SynchronizedKey) {
            return ((SynchronizedKey) object).getUuid().equals(getUuid());
        }

        return false;
    }
}
