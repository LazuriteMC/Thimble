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

    public static SynchronizedKey<?> get(Identifier identifier) {
        return get(identifier.toString());
    }

    public static SynchronizedKey<?> get(String identifier) {
        for (SynchronizedKey<?> entry : entries) {
            if (entry.toString().equals(identifier)) {
                return entry;
            }
        }

        return null;
    }

    /**
     * This exception is typically used if the user tries to use a {@link SynchronizedKey}
     * that isn't registered here in {@link SynchronizedKeyRegistry}.
     */
    public static class SynchronizedKeyException extends RuntimeException {
        public SynchronizedKeyException(String errorMessage) {
            super(errorMessage);
        }
    }
}
