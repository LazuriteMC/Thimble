package dev.lazurite.thimble.mixin;

import dev.lazurite.thimble.composition.Composition;
import dev.lazurite.thimble.composition.packet.AttachCompS2C;
import dev.lazurite.thimble.composition.register.CompositionRegistry;
import dev.lazurite.thimble.composition.register.CompositionTracker;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * This mixin mainly deals with executing {@link dev.lazurite.thimble.composition.Composition}
 * code within the {@link Entity#tick()} method.
 */
@Mixin(Entity.class)
public class EntityMixin {
    /**
     * The actual {@link Entity} object that this class is mixing into.
     */
    private final Entity entity = (Entity) (Object) this;

    /**
     * Injected right after readCustomDataFromTag is called,
     * this mixin reads {@link Composition} data from disk.
     * @param tag the {@link CompoundTag} to read from
     * @param info required by every mixin injection
     */
    @Inject(
            method = "fromTag",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;readCustomDataFromTag(Lnet/minecraft/nbt/CompoundTag;)V"
            )
    )
    public void fromTag(CompoundTag tag, CallbackInfo info) {
        /* The number of compositions associated with this entity */
        int compositionNumber = tag.getInt("CompositionCount");

        for (int i = 0; i < compositionNumber; i++) {
            String identifier = tag.getString("Composition" + i);

            if (identifier != null) {
                Composition composition = CompositionRegistry.get(identifier);

                if (composition != null) {
                    /* Attach the newly created composition */
                    CompositionTracker.attach(composition, entity);
                }
            }
        }
    }

    /**
     * Injected right after readCustomDataFromTag is called,
     * this mixin writes {@link Composition} data to disk.
     * @param tag the {@link CompoundTag} to write to
     * @param info required by every mixin injection
     */
    @Inject(
            method = "toTag",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;writeCustomDataToTag(Lnet/minecraft/nbt/CompoundTag;)V"
            )
    )
    public void toTag(CompoundTag tag, CallbackInfoReturnable<CompoundTag> info) {
        List<Composition> compositions = CompositionTracker.get(entity);

        if (!compositions.isEmpty()) {
            /* Write the number of compositions associated with this entity. */
            tag.putInt("CompositionCount", compositions.size());
            System.out.println("SIZE: " + compositions.size());

            /* Write each composition in the form of "Composition1, Composition2, Composition3, etc." */
            for (int i = 0; i < compositions.size(); i++) {
                tag.putString("Composition" + i, compositions.get(i).getIdentifier().toString());
            }
        }
    }

    /**
     * This injection allows for {@link dev.lazurite.thimble.composition.Composition}
     * object to be executed in their respective entity class.
     * @param info required by every mixin injection
     */
    @Inject(method = "tick()V", at = @At("HEAD"))
    public void tick(CallbackInfo info) {
        /*
         * All generic compositions associated with this entity. These
         * come first in order to get the general ones out of the way.
         */
        CompositionTracker.get(entity.getClass()).forEach(entry -> {
            entry.getSynchronizer().tick();
            entry.tick(entity);
        });

        /*
         * All unique compositions associated with this entity. It specifically
         * comes after generic compositions so that they may override them.
         */
        CompositionTracker.get(entity).forEach(entry -> {
            if (!entity.getEntityWorld().isClient()) {
                AttachCompS2C.send(entry, entity);
            }

            entry.getSynchronizer().tick();
            entry.tick(entity);
        });
    }
}
