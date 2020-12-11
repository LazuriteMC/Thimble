package dev.lazurite.thimble.composition.register;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.lazurite.thimble.composition.Composition;
import net.minecraft.entity.Entity;

import java.util.List;
import java.util.Map;

/**
 * Used for tracking active compositions. You can attach
 * any composition to any entity or entity type using
 * the {@link CompositionTracker#attach(Composition, Entity)}
 * or the {@link CompositionTracker#attach(Composition, Class)} methods.
 * @author Ethan Johnson
 */
public class CompositionTracker {
    /**
     * Map of all generic {@link Composition} objects.
     */
    private static final Map<Class<? extends Entity>, Composition> generic = Maps.newHashMap();

    /**
     * Map of all unique {@link Composition} objects.
     */
    private static final Map<Entity, Composition> unique = Maps.newHashMap();

    /**
     * Attaches a unique {@link Composition} to the given {@link Entity} object.
     * @param composition the {@link Composition}
     */
    public static void attach(Composition composition, Entity entity) {
        /* Throw an error if the composition isn't registered */
        if (CompositionRegistry.get(composition.getIdentifier()) == null) {
            throw new CompositionRegistry.CompositionRegistryException("Unable to attach unregistered composition");
        }

        /* Check if it's a duplicate */
        if (unique.containsValue(composition) && unique.containsKey(entity)) {
            return;
        }

        /* Add it to the map */
        unique.put(entity, composition);
    }

    /**
     * Attaches a generic {@link Composition} to an {@link Entity} class type.
     * @param composition the {@link Composition}
     */
    public static void attach(Composition composition, Class<? extends Entity> type) {
        /* Throw an error if the composition isn't registered */
        if (CompositionRegistry.get(composition.getIdentifier()) == null) {
            throw new CompositionRegistry.CompositionRegistryException("Unable to attach unregistered composition");
        }

        /* Check if it's a duplicate */
        if (generic.containsValue(composition) && generic.containsKey(type)) {
            return;
        }

        /* Add it to the map */
        generic.put(type, composition);
    }

    /**
     * Gets all {@link Composition} objects associated with
     * the given {@link Entity}. Returns an empty list if there
     * are none.
     * @param entity the {@link Entity} to find {@link Composition} objects for
     * @return a list of {@link Composition} objects
     */
    public static List<Composition> get(Entity entity) {
        List<Composition> out = Lists.newArrayList();

        for (Map.Entry<Entity, Composition> entry : unique.entrySet()) {
            if (entry.getKey().equals(entity)) {
                out.add(entry.getValue());
            }
        }

        return out;
    }

    /**
     * Gets all {@link Composition} objects associated with
     * the given class. Returns an empty list if there are none.
     * @return a list of {@link Composition} objects
     */
    public static List<Composition> get(Class<? extends Entity> type) {
        List<Composition> out = Lists.newArrayList();

        for (Map.Entry<Class<? extends Entity>, Composition> entry : generic.entrySet()) {
            if (entry.getKey().equals(type)) {
                out.add(entry.getValue());
            }
        }

        return out;
    }
}
