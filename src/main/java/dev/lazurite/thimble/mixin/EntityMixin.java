package dev.lazurite.thimble.mixin;

import dev.lazurite.thimble.Thimble;
import dev.lazurite.thimble.composition.Composition;
import dev.lazurite.thimble.composition.packet.StitchCompositionS2C;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * This mixin mainly deals with giving {@link Composition}
 * objects hooks into their stitched {@link Entity} objects.
 * @author Ethan Johnson
 */
@Mixin(Entity.class)
public class EntityMixin {
    /**
     * The actual {@link Entity} object that this class is mixing into.
     */
    private final Entity entity = (Entity) (Object) this;

    /**
     * Injected right before readCustomDataFromTag is called,
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
        int compositionNumber = tag.getInt("cc");

        /* Loop through to retrieve each Composition from the tag */
        for (int i = 0; i < compositionNumber; i++) {
            Identifier identifier = new Identifier(tag.getString("c" + i));
            Composition composition = Thimble.getRegistered(identifier).create();

            /* Attach the newly created composition */
            Thimble.stitch(composition, entity);

            /* Loads the synchronizer from the tag also */
            composition.getSynchronizer().fromTag(tag);
        }
    }

    /**
     * Injected right before writeCustomDataToTag is called,
     * this mixin writes {@link Composition} data to disk.
     * @param tag the {@link CompoundTag} to write to
     * @param info required by every mixin injection
     */
    @Inject(
            method = "toTag",
            cancellable = true,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;writeCustomDataToTag(Lnet/minecraft/nbt/CompoundTag;)V"
            )
    )
    public void toTag(CompoundTag tag, CallbackInfoReturnable<CompoundTag> info) {
        List<Composition> compositions = Thimble.getStitches(entity);

        if (!compositions.isEmpty()) {
            /* Write the number of compositions associated with this entity. */
            tag.putInt("cc", compositions.size());

            for (int i = 0; i < compositions.size(); i++) {
                /* Write each composition in the form of "c1, c2, c3, etc." */
                tag.putString("c" + i, compositions.get(i).getIdentifier().toString());

                /* Write the composition's synchronizer object */
                compositions.get(i).getSynchronizer().toTag(tag);
            }
        }
    }

    /**
     * This injection allows for {@link Composition} objects
     * to be executed in their respective entity class.
     * @param info required by every mixin injection
     */
    @Inject(method = "tick()V", at = @At("HEAD"))
    public void tick(CallbackInfo info) {
        /* All generic stitches associated with this entity */
        Thimble.getStitches(entity.getClass()).forEach(entry -> {
            entry.getSynchronizer().tick(entity.getEntityWorld());
            entry.tick(entity);
        });

        /* All unique stitches associated with this entity */
        Thimble.getStitches(entity).forEach(entry -> {
            if (!entity.getEntityWorld().isClient()) {
                StitchCompositionS2C.send(entry, entity);
            }

            entry.getSynchronizer().tick(entity.getEntityWorld());
            entry.tick(entity);
        });
    }

    /**
     * This injection provides a way for {@link Composition} objects
     * to detect a {@link PlayerEntity} right-click action on the
     * {@link Entity} it is stitched into.
     * @param player the {@link PlayerEntity} who right-clicked
     * @param hand the {@link Hand} of the {@link PlayerEntity}
     * @param info required by every mixin injection
     */
    @Inject(method = "interact", at = @At("TAIL"))
    public void interact(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> info) {
        /* Gets all generic stitches associated with this entity */
        Thimble.getStitches(entity.getClass()).forEach(entry -> entry.interact(player, hand));

        /* Gets all unique stitches associated with this entity */
        Thimble.getStitches(entity).forEach(entry -> entry.interact(player, hand));
    }

    /**
     * This injection provides a way for {@link Composition}
     * objects to detect when it's stitched {@link Entity}
     * is removed from the {@link net.minecraft.world.World}.
     * @param info required by every mixin injection
     */
    @Inject(method = "remove", at = @At("TAIL"))
    public void remove(CallbackInfo info) {
        /* Gets all generic stitches associated with this entity */
        Thimble.getStitches(entity.getClass()).forEach(Composition::remove);

        /* Gets all unique stitches associated with this entity */
        Thimble.getStitches(entity).forEach(Composition::remove);
    }
}
