package dev.lazurite.thimble.synchronizer;

import com.google.common.collect.Maps;
import dev.lazurite.thimble.synchronizer.key.SynchronizedKey;
import net.minecraft.nbt.CompoundTag;

import java.util.Map;
import java.util.UUID;

public class Synchronizer {
    private final Map<UUID, Entry<?>> entries = Maps.newHashMap();

    public Synchronizer() {

    }

    public void tick() {
        this.entries.forEach((uuid, entry) -> {
            if (entry.dirty) {
                // send packet
            }
        });
    }

    public <T> void track(SynchronizedKey<T> key) {
        entries.put(key.getUuid(), new Entry<>(key, key.getFallback()));
    }

    public <T> void set(SynchronizedKey<T> key, T value) {
        if (value == null) {
            return;
        }

        entries.replace(key.getUuid(), new Entry<>(key, value));
    }

    @SuppressWarnings("unchecked")
    public <T> T get(SynchronizedKey<T> key) {
        Entry<T> entry = (Entry<T>) entries.get(key.getUuid());
        return entry.getValue();
    }

    public Map<UUID, Entry<?>> getAll() {
        return this.entries;
    }

    public void toTag(CompoundTag tag) {
//        entries.forEach((uuid, entry) -> {
//            SynchronizedKey<?> key = entry.getKey();
//            key.getType().toTag(tag, key.getIdentifier(), entry.getValue());
//        });
    }

    public void fromTag(CompoundTag tag) {

    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Synchronizer) {
            return ((Synchronizer) obj).getAll().equals(getAll());
        }

        return false;
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
            return key.getType().copy(this.value);
        }
    }
}
