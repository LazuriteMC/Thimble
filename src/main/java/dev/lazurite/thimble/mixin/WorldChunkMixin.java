package dev.lazurite.thimble.mixin;

import dev.lazurite.thimble.Thimble;
import net.minecraft.entity.Entity;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * This mixin class only modifies the addEntity method in order to
 * make generic {@link dev.lazurite.thimble.composition.Composition}
 * objects possible.
 * @author Ethan Johnson
 */
@Mixin(WorldChunk.class)
public class WorldChunkMixin {
    /**
     * Stitches all generic {@link dev.lazurite.thimble.composition.Composition}
     * objects to an {@link Entity} when it spawns.
     * @param entity the {@link Entity} that is being added
     * @param info required by every mixin injection
     */
    @Inject(method = "addEntity", at = @At("HEAD"))
    public void addEntity(Entity entity, CallbackInfo info) {
        Thimble.getStitches(entity.getClass()).forEach(factory -> Thimble.stitch(factory, entity));
    }
}
