package dev.lazurite.thimble.example.item;

import dev.lazurite.thimble.composition.register.CompositionTracker;
import dev.lazurite.thimble.example.composition.FloatAwayComposition;
import dev.lazurite.thimble.example.composition.ParticleComposition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.List;

public class WandItem extends Item {
    public WandItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (!world.isClient()) {
            HitResult hitResult = raycast(world, user, RaycastContext.FluidHandling.NONE);

            if (hitResult.getType() == HitResult.Type.MISS) {
                List<Entity> list = world.getOtherEntities(user, new Box(new BlockPos(hitResult.getPos())).expand(16));
                System.out.println(list.size());
//                list.forEach(entity -> CompositionTracker.attach(new FloatAwayComposition(0.2f), entity));
                list.forEach(entity -> CompositionTracker.attach(new ParticleComposition(), entity));

                user.playSound(SoundEvents.BLOCK_ANVIL_HIT, 1.0f, 1.0f);
                return TypedActionResult.success(itemStack);
            }
        }

        return TypedActionResult.pass(itemStack);
    }
}
