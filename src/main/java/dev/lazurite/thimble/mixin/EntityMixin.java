package dev.lazurite.thimble.mixin;

import dev.lazurite.thimble.Thimble;
import dev.lazurite.thimble.composition.Composition;
import dev.lazurite.thimble.composition.CompositionFactory;
import dev.lazurite.thimble.composition.packet.StitchCompositionS2C;
import dev.lazurite.thimble.synchronizer.Synchronizer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
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
            /* Retrieve the "inner" tag */
            CompoundTag innerTag = tag.getCompound("c" + i);

            /* Get the composition factory */
            CompositionFactory factory = Thimble.getRegistered(new Identifier(tag.getString("compositionId")));

            /* Make a blank synchronizer */
            Synchronizer synchronizer = new Synchronizer(Synchronizer.NULL_UUID);

            /* Stitch the composition */
            Thimble.stitch(factory, entity, synchronizer);

            /* Read the synchronizer from the "inner" tag */
            synchronizer.fromTag(innerTag);
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
                /* The composition for this loop iteration */
                Composition composition = compositions.get(i);

                /* The synchronizer belonging to the current composition */
                Synchronizer synchronizer = composition.getSynchronizer();

                /* A new "inner" tag to write to */
                CompoundTag innerTag = new CompoundTag();

                /* Add the new "inner" tag */
                tag.put("c" + i, innerTag);

                /* Write the composition's identifier to the "inner" tag */
                innerTag.putString("compositionId", composition.getIdentifier().toString());

                /* Write the synchronizer to the "inner" tag */
                composition.getSynchronizer().toTag(innerTag);
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
     * objects to detect when their stitched {@link Entity}
     * takes damage
     * @param source the source of damage
     * @param amount the amount of damage taken
     * @param info required by every mixin injection
     */
    @Inject(method = "damage", at = @At("TAIL"))
    public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
        /* Gets all generic stitches associated with this entity */
        Thimble.getStitches(entity.getClass()).forEach(entry -> entry.damage(source, amount));

        /* Gets all unique stitches associated with this entity */
        Thimble.getStitches(entity).forEach(entry -> entry.damage(source, amount));
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
