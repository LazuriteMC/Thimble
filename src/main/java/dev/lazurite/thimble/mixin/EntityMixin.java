package dev.lazurite.thimble.mixin;

import dev.lazurite.thimble.composition.packet.AttachCompS2C;
import dev.lazurite.thimble.composition.register.AttachedCompositions;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * This mixin mainly deals with executing {@link dev.lazurite.thimble.composition.Composition}
 * code within the {@link Entity#tick()} method.
 */
@Mixin(Entity.class)
public class EntityMixin {
    /**
     * This injection allows for {@link dev.lazurite.thimble.composition.Composition}
     * object to be executed in their respective entity class.
     * @param info required by every mixin injection
     */
    @Inject(method = "tick()V", at = @At("HEAD"))
    public void tick(CallbackInfo info) {
        Entity entity = ((Entity) (Object) this);

        /*
         * All generic compositions associated with this entity. These
         * come first in order to get the general ones out of the way.
         */
        AttachedCompositions.get(entity.getClass()).forEach(entry -> {
            entry.getSynchronizer().tick();
            entry.tick(entity);
        });

        /*
         * All unique compositions associated with this entity. It specifically
         * comes after generic compositions so that they may override them.
         */
        AttachedCompositions.get(entity).forEach(entry -> {
            if (!entity.getEntityWorld().isClient()) {
                AttachCompS2C.send(entry, entity);
            }

            entry.getSynchronizer().tick();
            entry.tick(entity);
        });
    }
}
