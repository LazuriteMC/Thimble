package dev.lazurite.thimble.composition.register;

import com.google.common.collect.Lists;
import dev.lazurite.thimble.composition.Composition;
import dev.lazurite.thimble.composition.CompositionFactory;
import net.minecraft.util.Identifier;

import java.util.List;

/**
 * This is the place where every {@link Composition} is registered.
 * @author Ethan Johnson
 */
public class CompositionRegistry {
    private static final List<CompositionFactory> compositions = Lists.newArrayList();

    /**
     * Used for registering a {@link Composition}. A
     * {@link CompositionFactory} is stored in place
     * of a {@link Composition} for ease of creation.
     * @param factory the given {@link CompositionFactory}
     */
    public static void register(CompositionFactory factory) {
        compositions.add(factory);
    }

    /**
     * Gets a {@link Composition} from the registry
     * given it's {@link Identifier} object.
     * @param identifier the given {@link Identifier}
     * @return the associated {@link Composition}
     */
    public static Composition get(Identifier identifier) {
        return get(identifier.toString());
    }

    /**
     * Gets a {@link Composition} from the registry given it's
     * {@link String} version of it's {@link Identifier} object.
     * @param identifier the given {@link String} representing the {@link Identifier}
     * @return the associated {@link Composition}
     */
    public static Composition get(String identifier) {
        for (CompositionFactory entry : compositions) {
            Composition composition = entry.create();

            if (composition.getIdentifier().toString().equals(identifier)) {
                return composition;
            }
        }

        return null;
    }

    /**
     * This exception is typically used if the user tries to attach a
     * {@link Composition} which isn't registered here in {@link CompositionRegistry}.
     */
    public static class CompositionRegistryException extends RuntimeException {
        public CompositionRegistryException(String errorMessage) {
            super(errorMessage);
        }
    }
}
