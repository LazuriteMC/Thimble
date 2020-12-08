package dev.lazurite.thimble.example.item;

import dev.lazurite.thimble.example.composition.FloatAwayComposition;
import dev.lazurite.thimble.composition.register.CompositionRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

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

                cowEntity.updatePosition(hitResult.getPos().x, hitResult.getPos().y, hitResult.getPos().z);
                CompositionRegistry.register(new FloatAwayComposition(0.2f), cowEntity);

                world.spawnEntity(cowEntity);

                itemStack.decrement(1);
                itemStack = new ItemStack(Items.AIR);

                return TypedActionResult.success(itemStack);
            }
        }

        return TypedActionResult.pass(itemStack);
    }
}
