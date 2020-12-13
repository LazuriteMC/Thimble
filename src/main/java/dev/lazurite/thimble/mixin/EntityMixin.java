package dev.lazurite.thimble.mixin;

import com.google.common.collect.Lists;
import dev.lazurite.thimble.Thimble;
import dev.lazurite.thimble.composition.Composition;
import dev.lazurite.thimble.composition.CompositionFactory;
import dev.lazurite.thimble.composition.packet.StitchCompositionS2C;
import dev.lazurite.thimble.synchronizer.Synchronizer;
import dev.lazurite.thimble.util.EntityCompositionsStorage;
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
public class EntityMixin implements EntityCompositionsStorage {
    /**
     * The actual {@link Entity} object that this class is mixing into.
     */
    private final Entity entity = (Entity) (Object) this;

    /**
     * The set of {@link Composition} objects that the {@link Entity} possesses.
     */
    private final List<Composition> compositions = Lists.newArrayList();

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
            CompositionFactory factory = Thimble.getRegistered(new Identifier(innerTag.getString("compositionId")));

            /* Make a blank synchronizer */
            Synchronizer synchronizer = new Synchronizer(innerTag.getUuid("synchronizerId"));

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

                /* A new "inner" tag to write to */
                CompoundTag innerTag = new CompoundTag();

                /* Add the new "inner" tag */
                tag.put("c" + i, innerTag);

                /* Write the composition's identifier to the "inner" tag */
                innerTag.putString("compositionId", composition.getIdentifier().toString());

                /* Write the synchronizer's uuid to the "inner" tag */
                innerTag.putUuid("synchronizerId", composition.getSynchronizer().getUuid());

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
        /* All stitches associated with this entity */
        for (Composition composition : getCompositions()) {
            if (!entity.getEntityWorld().isClient()) {
                StitchCompositionS2C.send(composition, entity);
            }

            composition.getSynchronizer().tick(entity);
            composition.onTick(entity);
        }
    }

    /**
     * This injection provides a way for {@link Composition} objects
     * to detect a {@link PlayerEntity} right-click action on the
     * {@link Entity} it is stitched into.
     * @param player the {@link PlayerEntity} who right-clicked
     * @param hand the {@link Hand} of the {@link PlayerEntity}
     * @param info required by every mixin injection
     */
    @Inject(method = "interact", at = @At("TAIL"), cancellable = true)
    public void interact(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> info) {
        boolean shouldSwingHand = false;

        /* Gets all stitches associated with this entity */
        for (Composition composition : getCompositions()) {
            if (composition.onInteract(player, hand)) {
                shouldSwingHand = true;
            }
        }

        if (shouldSwingHand) {
            info.setReturnValue(ActionResult.SUCCESS);
        }
    }

    /**
     * This injection provides a way for {@link Composition}
     * objects to detect when it's stitched {@link Entity}
     * is removed from the {@link net.minecraft.world.World}.
     * @param info required by every mixin injection
     */
    @Inject(method = "remove", at = @At("HEAD"))
    public void remove(CallbackInfo info) {
        /* Gets all stitches associated with this entity */
        getCompositions().forEach(Composition::onRemove);
    }

    /**
     * Add a {@link Composition} to this specific {@link Entity}.
     * @param composition the {@link Composition} to add
     */
    @Override
    public void addComposition(Composition composition) {
        if (!compositions.contains(composition)) {
            compositions.add(composition);
        }
    }

    /**
     * Get all the {@link Composition} objects from this specific {@link Entity}.
     * @return the list of {@link Composition} objects
     */
    @Override
    public List<Composition> getCompositions() {
        return compositions;
    }
}
