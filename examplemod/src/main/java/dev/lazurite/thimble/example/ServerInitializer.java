package dev.lazurite.thimble.example;

import dev.lazurite.thimble.example.component.CowComponent;
import dev.lazurite.thimble.example.item.TestItem;
import dev.lazurite.thimble.registry.ComponentRegistry;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ServerInitializer implements ModInitializer {
    public static final String MODID = "examplemod";

    public static TestItem TEST_ITEM;

    static {
        ComponentRegistry.register(CowComponent::new);
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
