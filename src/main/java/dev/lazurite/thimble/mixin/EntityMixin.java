package dev.lazurite.thimble.mixin;

import dev.lazurite.thimble.composition.CompositionRegistry;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "tick()V", at = @At("HEAD"))
    public void tick(CallbackInfo info) {
        Entity entity = ((Entity) (Object) this);

        /* All generic compositions associated with this entity */
        CompositionRegistry.get(entity.getClass()).forEach(entry -> entry.tick(entity));

        /*
         * All unique compositions associated with this entity. Specifically comes after
         * generic compositions so that they may override them.
         */
        CompositionRegistry.get(entity).forEach(entry -> entry.tick(entity));
    }
}
