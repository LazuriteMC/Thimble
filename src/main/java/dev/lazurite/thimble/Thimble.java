package dev.lazurite.thimble;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.lazurite.thimble.composition.Composition;
import dev.lazurite.thimble.composition.CompositionFactory;
import dev.lazurite.thimble.exception.CompositionRegistryException;
import dev.lazurite.thimble.synchronizer.Synchronizer;
import dev.lazurite.thimble.util.EntityCompositionsStorage;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;
import java.util.UUID;

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
     * The method called in order to register a {@link Composition}.
     * @param factory the {@link CompositionFactory} used to create a new {@link Composition} object
     */
    public static void register(CompositionFactory factory) {
        registry.put(factory.create(new Synchronizer(Synchronizer.NULL_UUID)).getIdentifier(), factory);
    }

    /**
     * Retrieves a registered {@link CompositionFactory} from the registry.
     * @param identifier the {@link Identifier} used to find the {@link CompositionFactory}
     * @return the associated {@link CompositionFactory}
     */
    public static CompositionFactory getRegistered(Identifier identifier) {
        return registry.get(identifier);
    }

    /**
     * Stitches a unique {@link Composition} to the given {@link Entity} object.
     * @param factory the {@link CompositionFactory} to generate the {@link Composition}
     * @param entity the {@link Entity} to stitch the {@link Composition} to
     */
    public static void stitch(CompositionFactory factory, Entity entity) {
        stitch(factory, entity, new Synchronizer(UUID.randomUUID()));
    }

    /**
     * Stitches a unique {@link Composition} to the given {@link Entity} object.
     * @param factory the {@link CompositionFactory} to generate the {@link Composition}
     * @param entity the {@link Entity} to stitch the {@link Composition} to
     * @param synchronizer the {@link Synchronizer} that the {@link Composition} will use
     */
    public static void stitch(CompositionFactory factory, Entity entity, Synchronizer synchronizer) {
        /* Get the Entity Composition Storage object */
        EntityCompositionsStorage entityStorage = (EntityCompositionsStorage) (Object) entity;

        /* Create the new composition */
        Composition composition = factory.create(synchronizer);

        /* Throw an error if the composition isn't registered */
        if (getRegistered(composition.getIdentifier()) == null) {
            throw new CompositionRegistryException("Unable to attach unregistered composition");
        }

        /* Add it to the map */
        entityStorage.addComposition(composition);
    }

    /**
     * Used when an {@link Entity} is removed.
     * @param entity the removed {@link Entity}
     * @param composition the {@link Composition} to remove
     */
    public static void remove(Entity entity, Composition composition) {
        ((EntityCompositionsStorage) (Object) entity).getCompositions().remove(composition);
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
        out.addAll(((EntityCompositionsStorage) (Object) entity).getCompositions());
        return out;
    }
}
