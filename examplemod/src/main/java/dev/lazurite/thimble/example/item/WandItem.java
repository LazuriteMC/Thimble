package dev.lazurite.thimble.example.item;

import dev.lazurite.thimble.composition.register.CompositionTracker;
import dev.lazurite.thimble.example.composition.FloatAwayComposition;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class WandItem extends Item {
    public WandItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (world.isClient()) {
            HitResult hitResult = MinecraftClient.getInstance().crosshairTarget;

            if (hitResult != null) {
                if (hitResult.getType() == HitResult.Type.ENTITY) {
                    CompositionTracker.attach(new FloatAwayComposition(0.2f), ((EntityHitResult) hitResult).getEntity());
                    return TypedActionResult.success(itemStack);
                }
            }
        }

        return TypedActionResult.pass(itemStack);
    }
}
