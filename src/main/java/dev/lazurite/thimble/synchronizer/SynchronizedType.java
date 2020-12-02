package dev.lazurite.thimble.synchronizer;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;

public interface SynchronizedType<T> {
    void write(PacketByteBuf data, T object);
    T read(PacketByteBuf packetByteBuf);

    void toTag(CompoundTag tag, String key, T value);
    T fromTag(CompoundTag tag, String key);

    Class<T> getClassType();
}
