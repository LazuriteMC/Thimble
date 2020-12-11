package dev.lazurite.thimble.synchronizer;

import com.google.common.collect.Lists;
import dev.lazurite.thimble.synchronizer.key.SynchronizedKey;
import dev.lazurite.thimble.synchronizer.key.SynchronizedKeyRegistry;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the main class used to sync information across the client
 * and the server. Each {@link dev.lazurite.thimble.composition.Composition}
 * object contains one of these.
 * @author Ethan Johnson
 */
public class Synchronizer {
    private final List<Entry<?>> entries = Lists.newArrayList();

    /**
     * Checks for dirty entries.
     */
    public void tick() {
        this.entries.forEach(entry -> {
            if (entry.dirty) {
                // send packet
            }
        });
    }

    /**
     * Begin tracking the given key. Throws
     * exception if it isn't registered.
     * @param key The {@link SynchronizedKey} to register
     * @param <T> the type of the {@link SynchronizedKey}
     */
    public <T> void track(SynchronizedKey<T> key) {
        if (SynchronizedKeyRegistry.get(key.getIdentifier()) == null) {
            throw new SynchronizedKeyRegistry.SynchronizedKeyException("Unable to use unregistered key");
        }

        entries.add(new Entry<>(key, key.getFallback()));
    }

    /**
     * Sets the value of the given {@link SynchronizedKey}
     * to the given value. Does not accept null values.
     * @param key the {@link SynchronizedKey} to change
     * @param value the value to change
     * @param <T> the type of the key/value
     */
    @SuppressWarnings("unchecked")
    public <T> void set(SynchronizedKey<T> key, T value) {
        if (value == null) return;

        for (Entry<?> entry : entries) {
            if (entry.getKey().equals(key)) {
                ((Entry<T>) entry).setValue(value);

                if (entry.getKey().getConsumer() != null) {
//                    ((Entry<T>) entry).getKey().getConsumer().accept(((Entry<T>) entry).getValue(), value);
                }
            }
        }
    }

    /**
     * Gets the value of the given {@link SynchronizedKey}.
     * @param key the {@link SynchronizedKey} to find the value of
     * @param <T> the type of the {@link SynchronizedKey}
     * @return the associated value (or null)
     */
    @SuppressWarnings("unchecked")
    public <T> T get(SynchronizedKey<T> key) {
        for (Entry<?> entry : entries) {
            if (entry.getKey().getIdentifier().equals(key.getIdentifier())) {
                return (T) entry.getValue();
            }
        }

        return null;
    }

    /**
     * Gets all the entries in the {@link Synchronizer}.
     * @return the list of entries
     */
    public List<Entry<?>> getAll() {
        return new ArrayList<>(this.entries);
    }

    /**
     * Convert this entire {@link SynchronizedKey} into a {@link CompoundTag}.
     * @param tag the {@link CompoundTag} to write to
     */
    public void toTag(CompoundTag tag) {
        entries.forEach(entry -> entry.toTag(tag));
    }

    /**
     * Read this entire {@link Synchronizer} from a {@link CompoundTag}.
     * @param tag the {@link CompoundTag} to read from
     */
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

    /**
     * A helper class used to organize a composition of information
     * which is mainly a key-value pair. It can also be marked as dirty.
     * @param <T> the type of the {@link Entry}
     */
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
