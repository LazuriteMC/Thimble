package dev.lazurite.thimble.example;

import dev.lazurite.thimble.composition.register.CompositionRegistry;
import dev.lazurite.thimble.example.composition.FloatAwayComposition;
import dev.lazurite.thimble.example.item.TestItem;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ServerInitializer implements ModInitializer {
    public static final String MODID = "examplemod";

    public static TestItem TEST_ITEM;

    static {
        CompositionRegistry.register(new FloatAwayComposition(), CowEntity.class);
    }

    @Override
    public void onInitialize() {
        TEST_ITEM = Registry.register(
            Registry.ITEM,
            new Identifier(MODID, "test_item"),
            new TestItem(new Item.Settings().maxCount(1).group(ItemGroup.MISC))
        );
    }
}
