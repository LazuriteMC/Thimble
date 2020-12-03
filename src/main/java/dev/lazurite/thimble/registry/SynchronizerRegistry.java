package dev.lazurite.thimble.registry;

import com.google.common.collect.Lists;
import dev.lazurite.thimble.synchronizer.Synchronizer;
import dev.lazurite.thimble.util.TickableList;

import java.util.List;

public class SynchronizerRegistry implements TickableList<Synchronizer> {
    private final List<Synchronizer> synchronizers = Lists.newArrayList();

    @Override
    public void add(Synchronizer entry) {
        synchronizers.add(entry);
    }

    @Override
    public void clear() {
        synchronizers.clear();
    }

    @Override
    public void tick() {
        synchronizers.forEach(Synchronizer::tick);
    }
}
