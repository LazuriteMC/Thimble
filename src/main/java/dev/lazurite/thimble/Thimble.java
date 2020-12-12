package dev.lazurite.thimble;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.lazurite.thimble.composition.Composition;
import dev.lazurite.thimble.composition.CompositionFactory;
import dev.lazurite.thimble.composition.exception.CompositionRegistryException;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;

/**
 * This is the main class that should be used by mod authors.
 * Every Thimble utility should be accessible from this class.
 * @author Ethan Johnson
 */
public class Thimble {
    /**
     * The {@link java.util.HashMap} containing all registered {@link CompositionFactory} objects.
     */
    private static final Map<Identifier, CompositionFactory> registry = Maps.newConcurrentMap();

    /**
     * The {@link java.util.HashMap} containing all generically-stitched {@link Composition} objects.
     */
    private static final Map<Class<? extends Entity>, List<Composition>> genericStitches = Maps.newConcurrentMap();

    /**
     * The {@link java.util.HashMap} containing all uniquely-stitched {@link Composition} objects.
     */
    private static final Map<Entity, List<Composition>> uniqueStitches = Maps.newConcurrentMap();

    /**
     * The method called in order to register a {@link Composition}.
     * @param factory the {@link CompositionFactory} used to create a new {@link Composition} object
     */
    public static void register(CompositionFactory factory) {
        registry.put(factory.create().getIdentifier(), factory);
    }

    /**
     * Retrieves a registered {@link CompositionFactory} from the registry.
     * @param identifier the {@link Identifier} used to find the {@link CompositionFactory}
     * @return the associated {@link CompositionFactory}
     */
    public static CompositionFactory getRegistered(Identifier identifier) {
        return registry.get(identifier);
    }

    public static void stitch(CompositionFactory factory, Class<? extends Entity> entity) {
        stitch(factory.create(), entity);
    }

    /**
     * Stitches a generic {@link Composition} to a {@link Class} of type {@link Entity}.
     * @param composition the {@link Composition}
     */
    public static void stitch(Composition composition, Class<? extends Entity> type) {
        /* Create a new array if it isn't there */
        genericStitches.computeIfAbsent(type, t -> Lists.newArrayList());

        /* Throw an error if the composition isn't registered */
        if (getRegistered(composition.getIdentifier()) == null) {
            throw new CompositionRegistryException("Unable to attach unregistered composition");
        }

        /* Check if it's a duplicate */
        for (Composition comp : genericStitches.get(type)) {
            if (comp.equals(composition)) {
                return;
            }
        }

        /* Add it to the map */
        genericStitches.get(type).add(composition);
    }

    public static void stitch(CompositionFactory factory, Entity entity) {
        stitch(factory.create(), entity);
    }

    /**
     * Stitches a unique {@link Composition} to the given {@link Entity} object.
     * @param composition the {@link Composition}
     */
    public static void stitch(Composition composition, Entity entity) {
        /* Create a new array if it isn't there */
        uniqueStitches.computeIfAbsent(entity, e -> Lists.newArrayList());

        /* Throw an error if the composition isn't registered */
        if (getRegistered(composition.getIdentifier()) == null) {
            throw new CompositionRegistryException("Unable to attach unregistered composition");
        }

        /* Check if it's a duplicate */
        for (Composition comp : uniqueStitches.get(entity)) {
            if (comp.equals(composition)) {
                return;
            }
        }

        /* Add it to the map */
        uniqueStitches.get(entity).add(composition);
    }

    /**
     * Gets all {@link Composition} objects associated with
     * the given {@link Entity}. Returns an empty list if there
     * are none.
     * @param entity the {@link Entity} to find {@link Composition} objects for
     * @return a list of {@link Composition} objects
     */
    public static List<Composition> getStitches(Entity entity) {
        List<Composition> out = Lists.newArrayList();

        if (uniqueStitches.containsKey(entity)) {
            out.addAll(uniqueStitches.get(entity));
        }

        return out;
    }

    /**
     * Gets all {@link Composition} objects associated with
     * the given class. Returns an empty list if there are none.
     * @return a list of {@link Composition} objects
     */
    public static List<Composition> getStitches(Class<? extends Entity> type) {
        List<Composition> out = Lists.newArrayList();

        if (genericStitches.containsKey(type)) {
            out.addAll(genericStitches.get(type));
        }

        return out;
    }
}
