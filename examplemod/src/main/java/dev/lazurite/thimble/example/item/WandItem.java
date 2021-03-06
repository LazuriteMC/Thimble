package dev.lazurite.thimble.example.item;

import dev.lazurite.thimble.Thimble;
import dev.lazurite.thimble.example.composition.TimeBombComposition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.List;

/**
 * This is just meant as a test item for the example compositions.
 * @author Ethan Johnson
 */
public class WandItem extends Item {
    /**
     * The default constructor.
     * @param settings the item settings
     */
    public WandItem(Settings settings) {
        super(settings);
    }

    /**
     * This method handles whenever a player right-clicks while holding this item.
     * @param world the world the item is in
     * @param user the player who right-clicked
     * @param hand the hand of the player who right-clicked
     * @return the action result (success, fail, etc.)
     */
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        HitResult hitResult = raycast(world, user, RaycastContext.FluidHandling.NONE);

        if (!world.isClient()) {
            /* Get a list of all entities in 16 blocks */
            List<Entity> list = world.getOtherEntities(user, new Box(new BlockPos(hitResult.getPos())).expand(16));

            /* Stitch a random composition to those entities */
            list.forEach(entity -> Thimble.stitch(TimeBombComposition::new, entity));

            return TypedActionResult.success(itemStack);
        }

        return TypedActionResult.pass(itemStack);
    }
}
