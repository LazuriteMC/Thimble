package dev.lazurite.thimble.composition.register;

import com.google.common.collect.Maps;
import dev.lazurite.thimble.composition.Composition;
import dev.lazurite.thimble.composition.CompositionFactory;

import java.util.Map;

/**
 * This is the place where every {@link Composition} is registered.
 * @author Ethan Johnson
 */
public class CompositionRegistry {
    private static final Map<Integer, CompositionFactory> compositions = Maps.newHashMap();
    private static int nextID;

    public static void register(CompositionFactory factory) {
        compositions.put(nextID, factory);
        ++nextID;
    }

    /**
     * Gets the {@link Composition} associated with the given int.
     * @return the {@link Composition}
     */
    public static Composition get(int id) {
        return compositions.get(id).create();
    }

    /**
     * Gets the int associated with the given {@link Composition}.
     * @return the int of the {@link Composition}
     */
    public static int get(Composition composition) {
        for (Map.Entry<Integer, CompositionFactory> entry : compositions.entrySet()) {
            if (entry.getValue().create().equals(composition)) {
                return entry.getKey();
            }
        }

        return -1;
    }

    /**
     * This exception is typically used if the user tries to attach a
     * {@link Composition} which isn't registered in {@link CompositionRegistry}.
     */
    public static class CompositionRegistryException extends RuntimeException {
        public CompositionRegistryException(String errorMessage) {
            super(errorMessage);
        }
    }
}
