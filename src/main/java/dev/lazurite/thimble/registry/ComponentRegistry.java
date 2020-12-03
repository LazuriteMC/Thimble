package dev.lazurite.thimble.registry;

import com.google.common.collect.Lists;
import dev.lazurite.thimble.component.Component;
import dev.lazurite.thimble.util.TickableList;

import java.util.List;

public class ComponentRegistry implements TickableList<Component> {
    private final List<Component> components = Lists.newArrayList();

    @Override
    public void add(Component entry) {
        components.add(entry);
    }

    @Override
    public void clear() {
        components.clear();
    }

    @Override
    public void tick() {
        for (Component component : components) {
            if (component.isDestroyed()) {
                components.remove(component);
            } else {
                component.tick();
            }
        }
    }
}
