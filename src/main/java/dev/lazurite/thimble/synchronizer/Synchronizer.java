package dev.lazurite.thimble.synchronizer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.lazurite.thimble.component.Component;
import net.minecraft.server.world.ServerWorld;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Synchronizer {
    private static final List<Synchronizer> synchronizers = Lists.newArrayList();

    private final Map<UUID, Entry<?>> entries = Maps.newHashMap();
    private final Component<?> component;

    public static void tick(ServerWorld world) {
        for (Synchronizer synchronizer : synchronizers) {
            synchronizer.entries.forEach((uuid, entry) -> {
                if (entry.dirty) {
                    // send packet
                }
            });
        }
    }

    public Synchronizer(Component<?> component) {
        this.component = component;

        synchronizers.add(this);
    }

    public <T> void track(SynchronizedKey<T> key) {
        entries.put(key.getUuid(), new Entry<>(key, key.getFallback()));
    }

    public <T> void track(SynchronizedKey<T> key, T value) {
        if (value == null) {
            entries.put(key.getUuid(), new Entry<>(key, key.getFallback()));
        } else {
            entries.put(key.getUuid(), new Entry<>(key, value));
        }
    }

    public <T> void set(SynchronizedKey<T> key, T value) {
        if (value == null) {
            return;
        }

        entries.replace(key.getUuid(), new Entry<>(key, value));
    }

    public <T> T get(SynchronizedKey<T> key) {
        Entry<T> entry = (Entry<T>) entries.get(key.getUuid());
        return entry.getValue();
    }

    public static List<Synchronizer> getAll() {
        return Lists.newArrayList(synchronizers);
    }

    public static class Entry<T> {
        private final SynchronizedKey<T> key;
        private final T value;
        private boolean dirty;

        public Entry(SynchronizedKey<T> key, T value) {
            this.key = key;
            this.value = value;
        }

        public SynchronizedKey<T> getKey() {
            return this.key;
        }

        public T getValue() {
            return this.value;
        }
    }
}
