package dev.lazurite.thimble.mixin;

import dev.lazurite.thimble.component.GenericComponent;
import dev.lazurite.thimble.registry.ComponentRegistry;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "tick()V", at = @At("HEAD"))
    public void tick(CallbackInfo info) {
        ComponentRegistry.get(((Entity) (Object) this).getClass()).forEach(GenericComponent::tick);
    }
}
