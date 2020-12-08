package dev.lazurite.thimble.synchronizer.register;

import com.google.common.collect.Maps;
import dev.lazurite.thimble.synchronizer.Synchronizer;

import java.util.Map;
import java.util.UUID;

public class SynchronizerRegistry {
    private static final Map<UUID, Synchronizer> synchronizers = Maps.newHashMap();

    public static void add(Synchronizer entry) {
        synchronizers.put(UUID.randomUUID(), entry);
    }

    public static Synchronizer get(UUID uuid) {
        return synchronizers.get(uuid);
    }

    public static void clear() {
        synchronizers.clear();
    }

    public static void tick() {
        for (Synchronizer synchronizer : synchronizers.values()) {
//            if (synchronizer.getComponent().getOwner().removed) {
//                synchronizers.remove(synchronizer);
//            } else {
//                synchronizer.tick();
//            }
        }
    }
}
