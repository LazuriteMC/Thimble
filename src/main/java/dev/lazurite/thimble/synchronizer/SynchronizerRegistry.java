package dev.lazurite.thimble.synchronizer;

import com.google.common.collect.Maps;
import dev.lazurite.thimble.synchronizer.Synchronizer;
import dev.lazurite.thimble.util.TickableList;

import java.util.Map;
import java.util.UUID;

public class SynchronizerRegistry implements TickableList<Synchronizer> {
    private final Map<UUID, Synchronizer> synchronizers = Maps.newHashMap();

    @Override
    public void add(Synchronizer entry) {
        synchronizers.put(UUID.randomUUID(), entry);
    }

    public Synchronizer get(UUID uuid) {
        return synchronizers.get(uuid);
    }

    @Override
    public void clear() {
        synchronizers.clear();
    }

    @Override
    public void tick() {
        for (Synchronizer synchronizer : synchronizers.values()) {
//            if (synchronizer.getComponent().getOwner().removed) {
//                synchronizers.remove(synchronizer);
//            } else {
//                synchronizer.tick();
//            }
        }
    }
}
