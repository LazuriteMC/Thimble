package dev.lazurite.thimble.component;

import com.google.common.collect.Lists;
import net.minecraft.server.world.ServerWorld;

import java.util.List;

public class Components {
    private static List<Component> components = Lists.newArrayList();

    public static void add(Component component) {
        components.add(component);
    }

    public static void tick(ServerWorld world) {
        for (Component component : components) {
            if (component.isDestroyed()) {
                components.remove(component);
            } else {
                component.tick();
            }
        }
    }
}
