package dev.lazurite.thimble.synchronizer;

import net.minecraft.client.util.math.Vector3f;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;

public class SynchronizedTypes {
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
        public Class<Integer> getClassType() {
            return int.class;
        }
    };

    public static final SynchronizedType<Vector3f> VECTOR3F = new SynchronizedType<Vector3f>() {
        @Override
        public void write(PacketByteBuf buf, Vector3f value) {
            buf.writeFloat(value.getX());
            buf.writeFloat(value.getY());
            buf.writeFloat(value.getZ());
        }

        @Override
        public Vector3f read(PacketByteBuf buf) {
            return new Vector3f(buf.readFloat(), buf.readFloat(), buf.readFloat());
        }

        @Override
        public void toTag(CompoundTag tag, String key, Vector3f value) {
            tag.putFloat(key + "x", value.getX());
            tag.putFloat(key + "y", value.getY());
            tag.putFloat(key + "z", value.getZ());
        }

        @Override
        public Vector3f fromTag(CompoundTag tag, String key) {
            return new Vector3f(tag.getFloat(key + "x"), tag.getFloat(key + "y"), tag.getFloat(key + "z"));
        }

        @Override
        public Class<Vector3f> getClassType() {
            return Vector3f.class;
        }
    };
}
