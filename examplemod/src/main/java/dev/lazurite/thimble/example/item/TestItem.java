package dev.lazurite.thimble.example.item;

import dev.lazurite.thimble.composition.register.CompositionTracker;
import dev.lazurite.thimble.example.composition.FloatAwayComposition;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.Random;

public class TestItem extends Item {
    public TestItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        HitResult hitResult = raycast(world, user, RaycastContext.FluidHandling.NONE);
        ItemStack itemStack = user.getStackInHand(hand);

        if (!world.isClient()) {
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                CowEntity cowEntity = EntityType.COW.create(world);
                Random random = new Random();

                if (cowEntity != null) {
                    cowEntity.updatePosition(hitResult.getPos().x, hitResult.getPos().y, hitResult.getPos().z);
                    CompositionTracker.attach(new FloatAwayComposition((random.nextInt(4)+1) / 10.0f), cowEntity);
                    world.spawnEntity(cowEntity);
                }

                return TypedActionResult.success(itemStack);
            }
        }

        return TypedActionResult.pass(itemStack);
    }
}
