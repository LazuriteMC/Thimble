package dev.lazurite.thimble.example;

import dev.lazurite.thimble.composition.register.CompositionRegistry;
import dev.lazurite.thimble.example.composition.FloatAwayComposition;
import dev.lazurite.thimble.example.composition.ParticleComposition;
import dev.lazurite.thimble.example.item.WandItem;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ServerInitializer implements ModInitializer {
    public static final String MODID = "examplemod";

    public static WandItem WAND_ITEM;

    static {
        CompositionRegistry.register(FloatAwayComposition::new);
        CompositionRegistry.register(ParticleComposition::new);
    }

    @Override
    public void onInitialize() {
        WAND_ITEM = Registry.register(
            Registry.ITEM,
            new Identifier(MODID, "wand_item"),
            new WandItem(new Item.Settings().maxCount(1).group(ItemGroup.MISC))
        );
    }
}
