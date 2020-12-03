package dev.lazurite.thimble.synchronizer;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;

public interface SynchronizedType<T> {
    void write(PacketByteBuf buf, T value);
    T read(PacketByteBuf buf);

    void toTag(CompoundTag tag, String key, T value);
    T fromTag(CompoundTag tag, String key);

    T copy(T value);
    Class<T> getClassType();
}
