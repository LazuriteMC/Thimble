package dev.lazurite.thimble.synchronizer;

import com.google.common.collect.Lists;
import dev.lazurite.thimble.synchronizer.key.SynchronizedKey;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.List;

public class Synchronizer {
    private final List<Entry<?>> entries = Lists.newArrayList();

    public Synchronizer() {

    }

    public void tick() {
        this.entries.forEach(entry -> {
            if (entry.dirty) {
                // send packet
            }
        });
    }

    public <T> void track(SynchronizedKey<T> key) {
        entries.add(new Entry<>(key, key.getFallback()));
    }

    @SuppressWarnings("unchecked")
    public <T> void set(SynchronizedKey<T> key, T value) {
        if (value == null) {
            return;
        }

        for (Entry<?> entry : entries) {
            if (entry.getKey().equals(key)) {
                ((Entry<T>) entry).setValue(value);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T get(SynchronizedKey<T> key) {
        for (Entry<?> entry : entries) {
            if (entry.getKey().getIdentifier().equals(key.getIdentifier())) {
                return (T) entry.getValue();
            }
        }

        return null;
    }

    public List<Entry<?>> getAll() {
        return new ArrayList<>(this.entries);
    }

    public void toTag(CompoundTag tag) {
        entries.forEach(entry -> entry.toTag(tag));
    }

    public void fromTag(CompoundTag tag) {
        entries.forEach(entry -> entry.fromTag(tag));
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
        private boolean dirty;
        private T value;

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

        public void setValue(T value) {
            this.value = value;
        }

        public void toTag(CompoundTag tag) {
            getKey().getType().toTag(tag, getKey().getIdentifier().toString(), getValue());
        }

        public void fromTag(CompoundTag tag) {
            setValue(getKey().getType().fromTag(tag, getKey().toString()));
        }
    }
}
