package dev.lazurite.thimble.composition;

import com.google.common.collect.Lists;
import dev.lazurite.thimble.util.IntMap;
import net.minecraft.entity.Entity;

import java.util.List;

/**
 * This is the place where every {@link Composition} is registered.
 * @author Ethan Johnson
 */
public class CompositionRegistry {
    private static final IntMap<GenericEntry<?>> genericRegistry = new IntMap<>();
    private static final IntMap<UniqueEntry<?>> uniqueRegistry = new IntMap<>();

    /**
     * Registers a unique {@link Composition} used on just one {@link Entity}.
     * @param composition the {@link Composition}
     */
    public static void register(Composition composition, Entity entity) {
        uniqueRegistry.add(new UniqueEntry<>(composition, entity));
    }

    /**
     * Registers a generic {@link Composition} used on every {@link Entity} of that type.
     * @param composition the {@link Composition}
     */
    public static void register(Composition composition, Class<? extends Entity> type) {
        genericRegistry.add(new GenericEntry<>(composition, type));
    }

    /**
     * Gets all {@link Composition} objects associated with
     * the given {@link Entity}. Returns an empty list if there
     * are none.
     * @param entity the {@link Entity} to find {@link Composition} objects for
     * @return a list of {@link Composition} objects
     */
    @SuppressWarnings("unchecked")
    public static List<Composition> get(Entity entity) {
        List<Composition> out = Lists.newArrayList();

        for (UniqueEntry<?> entry : uniqueRegistry.getAll()) {
            if (entry.getEntity().equals(entity)) {
                out.add(entry.getComposition());
            }
        }

        return out;
    }

    /**
     * Gets all {@link Composition} objects associated with
     * the given class. Returns an empty list if there are none.
     * @return a list of {@link Composition} objects
     */
    @SuppressWarnings("unchecked")
    public static List<Composition> get(Class<? extends Entity> type) {
        List<Composition> out = Lists.newArrayList();

        for (GenericEntry<?> entry: genericRegistry.getAll()) {
            if (entry.getType().equals(type)) {
                out.add(entry.getComposition());
            }
        }

        return out;
    }

    static class GenericEntry<U extends Entity> {
        private final Composition composition;
        private final Class<U> type;

        public GenericEntry(Composition composition, Class<U> type) {
            this.composition = composition;
            this.type = type;
        }

        public Composition getComposition() {
            return this.composition;
        }

        public Class<U> getType() {
            return this.type;
        }
    }

    static class UniqueEntry<U extends Entity> {
        private final Composition composition;
        private final U entity;

        public UniqueEntry(Composition composition, U entity) {
            this.composition = composition;
            this.entity = entity;
        }

        public Composition getComposition() {
            return this.composition;
        }

        public U getEntity() {
            return this.entity;
        }
    }
}
