package dev.lazurite.thimble.mixin;

import dev.lazurite.thimble.Thimble;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * This mixin class only modifies the {@link ServerWorld#spawnEntity} method
 * in order to make generic {@link dev.lazurite.thimble.composition.Composition}
 * objects possible.
 * @author Ethan Johnson
 */
@Mixin(ServerWorld.class)
public class ServerWorldMixin {
    /**
     * Stitches all generic {@link dev.lazurite.thimble.composition.Composition}
     * objects to an {@link Entity} when it spawns.
     * @param entity the {@link Entity} to stitch {@link dev.lazurite.thimble.composition.Composition} objects to
     * @param info required by every mixin injection
     */
    @Inject(method = "spawnEntity", at = @At("HEAD"))
    public void spawnEntity(Entity entity, CallbackInfoReturnable<Boolean> info) {
        Thimble.getStitches(entity.getClass()).forEach(factory -> Thimble.stitch(factory, entity));
    }
}
