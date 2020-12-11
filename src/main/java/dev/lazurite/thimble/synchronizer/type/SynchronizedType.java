package dev.lazurite.thimble.synchronizer.type;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;

/**
 * An interface that serves a reference for when
 * a synchronized value is stored in a {@link CompoundTag}
 * or send over the network in a {@link PacketByteBuf}.
 * See {@link SynchronizedTypeRegistry} for information
 * on creating your own custom {@link SynchronizedType}.
 * @param <T> the type of the {@link SynchronizedType}
 * @author Ethan Johnson
 */
public interface SynchronizedType<T> {
    void write(PacketByteBuf buf, T value);
    T read(PacketByteBuf buf);

    void toTag(CompoundTag tag, String key, T value);
    T fromTag(CompoundTag tag, String key);

    T copy(T value);
    Class<T> getClassType();
}
