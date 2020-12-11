package dev.lazurite.thimble.synchronizer.type;

import com.google.common.collect.Maps;
import net.fabricmc.api.ModInitializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;

import java.util.Map;
import java.util.UUID;

/**
 * This is the registry for {@link SynchronizedType} objects. It's
 * basically just a list of {@link SynchronizedType} objects you have
 * available to you. If you would like to use a type not listed here, you
 * can write your own by implementing the {@link SynchronizedType} interface.
 * If you write your own, don't forget to register it here by calling
 * {@link SynchronizedTypeRegistry#register} in a static context, not somewhere
 * like {@link ModInitializer#onInitialize()}.
 *
 * DEFAULT TYPES:
 *    * Boolean
 *    * Float
 *    * Integer
 *    * UUID
 *
 * @author Ethan Johnson
 */
public class SynchronizedTypeRegistry {
    private static final Map<UUID, SynchronizedType<?>> entries = Maps.newHashMap();

    public static final SynchronizedType<Boolean> BOOLEAN = new SynchronizedType<Boolean>() {
        @Override
        public void write(PacketByteBuf buf, Boolean value) {
            buf.writeBoolean(value);
        }

        @Override
        public Boolean read(PacketByteBuf buf) {
            return buf.readBoolean();
        }

        @Override
        public void toTag(CompoundTag tag, String key, Boolean value) {
            tag.putBoolean(key, value);
        }

        @Override
        public Boolean fromTag(CompoundTag tag, String key) {
            return tag.getBoolean(key);
        }

        @Override
        public Boolean copy(Boolean value) {
            return value;
        }

        @Override
        public Class<Boolean> getClassType() {
            return Boolean.class;
        }
    };

    public static final SynchronizedType<Float> FLOAT = new SynchronizedType<Float>() {
        @Override
        public void write(PacketByteBuf buf, Float value) {
            buf.writeFloat(value);
        }

        @Override
        public Float read(PacketByteBuf buf) {
            return buf.readFloat();
        }

        @Override
        public void toTag(CompoundTag tag, String key, Float value) {
            tag.putFloat(key, value);
        }

        @Override
        public Float fromTag(CompoundTag tag, String key) {
            return tag.getFloat(key);
        }

        @Override
        public Float copy(Float value) {
            return value;
        }

        @Override
        public Class<Float> getClassType() {
            return Float.class;
        }
    };

    public static final SynchronizedType<Integer> INTEGER = new SynchronizedType<Integer>() {
        @Override
        public void write(PacketByteBuf buf, Integer value) {
            buf.writeInt(value);
        }

        @Override
        public Integer read(PacketByteBuf buf) {
            return buf.readInt();
        }

        @Override
        public void toTag(CompoundTag tag, String key, Integer value) {
            tag.putInt(key, value);
        }

        @Override
        public Integer fromTag(CompoundTag tag, String key) {
            return tag.getInt(key);
        }

        @Override
        public Integer copy(Integer value) {
            return value;
        }

        @Override
        public Class<Integer> getClassType() {
            return int.class;
        }
    };

    public static final SynchronizedType<UUID> UUID = new SynchronizedType<java.util.UUID>() {
        @Override
        public void write(PacketByteBuf buf, java.util.UUID value) {
            buf.writeUuid(value);
        }

        @Override
        public java.util.UUID read(PacketByteBuf buf) {
            return buf.readUuid();
        }

        @Override
        public void toTag(CompoundTag tag, String key, java.util.UUID value) {
            tag.putUuid(key, value);
        }

        @Override
        public java.util.UUID fromTag(CompoundTag tag, String key) {
            return tag.getUuid(key);
        }

        @Override
        public java.util.UUID copy(java.util.UUID value) {
            return UUID.copy(value);
        }

        @Override
        public Class<java.util.UUID> getClassType() {
            return UUID.class;
        }
    };

    /**
     * Register the type in the list of entries.
     * @param synchronizedType the type to be registered
     * @param <T> the type of the {@link SynchronizedType} object
     */
    public static <T> void register(SynchronizedType<T> synchronizedType) {
        entries.put(java.util.UUID.randomUUID(), synchronizedType);
    }

    /*
     * This is where the default types are registered.
     */
    static {
        register(BOOLEAN);
        register(INTEGER);
        register(FLOAT);
        register(UUID);
    }
}
