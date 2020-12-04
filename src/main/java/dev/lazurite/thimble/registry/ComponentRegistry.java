package dev.lazurite.thimble.registry;

import com.google.common.collect.Lists;
import dev.lazurite.thimble.component.Component;
import net.minecraft.entity.Entity;

import java.util.List;

public class ComponentRegistry {
    private static final List<Component<?>> components = Lists.newArrayList();

    public static void add(Component<?> entry) {
        components.add(entry);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Entity> List<Component<T>> get(T entity) {
        List<Component<T>> out = Lists.newArrayList();

        for (Component<?> component : components) {
            if (component.getOwner().equals(entity)) {
                out.add((Component<T>) component);
            }
        }

        return out;
    }

    public static void clear() {
        components.clear();
    }
}
