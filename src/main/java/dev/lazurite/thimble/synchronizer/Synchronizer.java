package dev.lazurite.thimble.synchronizer;

import com.google.common.collect.Lists;
import dev.lazurite.thimble.exception.SynchronizedKeyException;
import dev.lazurite.thimble.synchronizer.key.SynchronizedKey;
import dev.lazurite.thimble.synchronizer.packet.SynchronizeEntryPacket;
import dev.lazurite.thimble.synchronizer.type.SynchronizedType;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This is the main class used to sync information across the client
 * and the server. Each {@link dev.lazurite.thimble.composition.Composition}
 * object contains one of these.
 * @author Ethan Johnson
 */
public class Synchronizer {
    /** An empty/null {@link UUID} object. */
    public static final UUID NULL_UUID = new UUID(0, 0);

    /** The list of registered keys. */
    private static final List<SynchronizedKey<?>> keyRegistry = Lists.newArrayList();

    /** The list of synchronized entries. */
    private final List<Entry<?>> entries = Lists.newArrayList();

    /** The {@link UUID} used for identifying this {@link Synchronizer}. */
    private final UUID uuid;

    /**
     * The constructor which takes a {@link UUID}
     * object which is used to identify the {@link Synchronizer}.
     * @param uuid the {@link UUID} used for identification
     */
    public Synchronizer(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * Sends a packet if an entry becomes dirty.
     * @param world the {@link World}, may be client or server side
     */
    public void tick(Entity entity) {
        this.entries.forEach(entry -> {
            if (entry.dirty) {
                entry.dirty = false;
                SynchronizeEntryPacket.send(this, entry, entity);
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
        if (getKey(key.getIdentifier()) == null) {
            throw new SynchronizedKeyException("Unable to use unregistered key");
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
    public <T> void set(SynchronizedKey<T> key, T value, boolean dirty) {
        /* Null values are not allowed */
        if (value == null) return;

        for (Entry<?> entry : entries) {
            if (entry.getKey().equals(key)) {
                /* Set the value in the list of entries */
                ((Entry<T>) entry).setValue(value);

                /* Execute the consumer if it exists */
                if (key.getConsumer() != null) {
                    key.getConsumer().accept(value);
                }

                entry.dirty = dirty;
            }
        }
    }

    /**
     * Sets the value of the given {@link SynchronizedKey}
     * to the value stored in the {@link PacketByteBuf}.
     * @param entry the {@link Entry} to change
     * @param <T> the type of the key/value
     */
    public <T> void set(Entry<T> entry) {
        set(entry.getKey(), entry.getValue(), false);
    }

    /**
     * Sets the value of the given {@link SynchronizedKey}
     * to the value stored in the {@link PacketByteBuf}.
     * @param key the {@link SynchronizedKey} to change
     * @param value the value to change
     * @param <T> the type of the key/value
     */
    public <T> void set(SynchronizedKey<T> key, T value) {
        set(key, value, true);
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
     * @return the {@link UUID} used to identify this {@link Synchronizer}
     */
    public UUID getUuid() {
        return this.uuid;
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
     * Registers a new {@link SynchronizedKey}.
     * @param identifier the key's {@link Identifier}
     * @param type the key's data type
     * @param fallback the key's fallback value
     * @param <T> the key and the value's generic type
     * @return the new {@link SynchronizedKey}
     */
    public static <T> SynchronizedKey<T> register(Identifier identifier, SynchronizedType<T> type, T fallback) {
        SynchronizedKey<T> key = new SynchronizedKey<>(identifier, type, fallback);
        keyRegistry.add(key);
        return key;
    }

    /**
     * Gets a registered key with the given {@link Identifier}.
     * @param identifier the {@link Identifier} used for finding the key
     * @return the registered {@link SynchronizedKey}
     */
    public static SynchronizedKey<?> getKey(Identifier identifier) {
        for (SynchronizedKey<?> key : keyRegistry) {
            if (key.getIdentifier().equals(identifier)) {
                return key;
            }
        }

        return null;
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
            getKey().getType().toTag(tag, getKey().toString(), getValue());
        }

        public void fromTag(CompoundTag tag) {
            dirty = true;
            setValue(getKey().getType().fromTag(tag, getKey().toString()));
        }
    }
}
