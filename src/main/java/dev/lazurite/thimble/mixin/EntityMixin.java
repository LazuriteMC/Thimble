package dev.lazurite.thimble.mixin;

import dev.lazurite.thimble.component.Component;
import dev.lazurite.thimble.registry.ComponentRegistry;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    public void tick() {
        ComponentRegistry.get((Entity) (Object) this).forEach(Component::tick);
    }
}
