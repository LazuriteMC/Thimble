package dev.lazurite.thimble.synchronizer.key;

import com.google.common.collect.Lists;
import dev.lazurite.thimble.synchronizer.type.SynchronizedType;
import net.minecraft.util.Identifier;

import java.util.List;

public class SynchronizedKeyRegistry {
    private static final List<SynchronizedKey<?>> entries = Lists.newArrayList();

    public static <T> SynchronizedKey<T> register(Identifier identifier, SynchronizedType<T> type, T fallback) {
        SynchronizedKey<T> key = new SynchronizedKey<>(identifier, type, fallback);
        entries.add(key);
        return key;
    }
}
