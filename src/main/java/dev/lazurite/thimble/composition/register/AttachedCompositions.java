package dev.lazurite.thimble.composition.register;

import com.google.common.collect.Lists;
import dev.lazurite.thimble.composition.Composition;
import net.minecraft.entity.Entity;

import java.util.List;

public class AttachedCompositions {
    private static final List<GenericEntry> generic = Lists.newArrayList();
    private static final List<UniqueEntry> unique = Lists.newArrayList();

    /**
     * Attaches a unique {@link Composition} to the given {@link Entity} object.
     * @param composition the {@link Composition}
     */
    public static void attach(Composition composition, Entity entity) {
        if (CompositionRegistry.get(composition) == -1) {
            throw new CompositionRegistry.CompositionRegistryException("Unable to attach unregistered composition");
        }

        unique.add(new UniqueEntry(composition, entity));
    }

    /**
     * Attaches a generic {@link Composition} to an {@link Entity} class type.
     * @param composition the {@link Composition}
     */
    public static void attach(Composition composition, Class<? extends Entity> type) {
        if (CompositionRegistry.get(composition) == -1) {
            throw new CompositionRegistry.CompositionRegistryException("Unable to attach unregistered composition");
        }

        generic.add(new GenericEntry(composition, type));
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

        for (UniqueEntry entry : unique) {
            if (entry.getEntity().equals(entity)) {
                out.addAll(entry.getCompositions());
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

        for (GenericEntry entry : generic) {
            if (entry.getType().equals(type)) {
                out.addAll(entry.getCompositions());
            }
        }

        return out;
    }

    static class GenericEntry {
        private final List<Composition> compositions = Lists.newArrayList();
        private final Class<? extends Entity> type;

        public GenericEntry(Composition composition, Class<? extends Entity> type) {
            this.compositions.add(composition);
            this.type = type;
        }

        public List<Composition> getCompositions() {
            return this.compositions;
        }

        public Class<? extends Entity> getType() {
            return this.type;
        }
    }

    static class UniqueEntry {
        private final List<Composition> compositions = Lists.newArrayList();
        private final Entity entity;

        public UniqueEntry(Composition composition, Entity entity) {
            this.compositions.add(composition);
            this.entity = entity;
        }

        public List<Composition> getCompositions() {
            return this.compositions;
        }

        public Entity getEntity() {
            return this.entity;
        }
    }
}
