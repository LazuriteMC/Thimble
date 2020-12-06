package dev.lazurite.thimble.registry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.lazurite.thimble.component.Component;
import dev.lazurite.thimble.component.GenericComponent;
import dev.lazurite.thimble.component.UniqueComponent;
import net.minecraft.entity.Entity;

import java.util.List;
import java.util.Map;

/**
 * This is the place where every component is registered.
 * @author Ethan Johnson
 */
public class ComponentRegistry {
    private static final Map<Integer, GenericComponent<?>> genericRegistry = Maps.newHashMap();
    private static final Map<Integer, UniqueCompFactory<?>> uniqueRegistry = Maps.newHashMap();
    private static int nextId;

    public interface UniqueCompFactory<T extends Entity> {
        Component create(T owner);
    }

    /**
     * Registers a generic component used on every entity of that type.
     * @param entry the component
     */
    public static <T extends Entity> void register(GenericComponent<T> entry) {
        genericRegistry.put(nextId, entry);
        ++nextId;
    }

    /**
     * Registers a unique component used on just the owner.
     * @param entry the component
     */
    public static <T extends Entity> void register(UniqueCompFactory<T> entry) {
        uniqueRegistry.put(nextId, entry);
        ++nextId;
    }

    /**
     * Gets all {@link UniqueComponent} objects associated with
     * the given {@link Entity}. Returns an empty list if there
     * are none.
     * @param entity the {@link Entity} to find {@link UniqueComponent} objects for
     * @param <T> the type of {@link Entity}
     * @return a list of {@link UniqueComponent} objects
     */
    @SuppressWarnings("unchecked")
    public static <T extends Entity> List<UniqueComponent<T>> get(T entity) {
        List<UniqueComponent<T>> out = Lists.newArrayList();

        for (UniqueComponent<?> component : uniqueRegistry) {
            if (component.getOwner().equals(entity)) {
                out.add((UniqueComponent<T>) component);
            }
        }

        return out;
    }

    /**
     * Gets all {@link GenericComponent} objects associated with
     * the given class. Returns an empty list if there are none.
     * @param <T> the type of class
     * @return a list of {@link GenericComponent} objects
     */
    @SuppressWarnings("unchecked")
    public static <T extends Entity> List<GenericComponent<T>> get(Class<T> type) {
        List<GenericComponent<T>> out = Lists.newArrayList();

        for (GenericComponent<?> component : genericRegistry) {
            if (component.getType().equals(type)) {
                out.add((GenericComponent<T>) component);
            }
        }

        return out;
    }
}
