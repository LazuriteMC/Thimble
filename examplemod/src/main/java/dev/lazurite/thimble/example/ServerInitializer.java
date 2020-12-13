package dev.lazurite.thimble.example;

import dev.lazurite.thimble.Thimble;
import dev.lazurite.thimble.example.composition.FloatAwayComposition;
import dev.lazurite.thimble.example.composition.TimeBombComposition;
import dev.lazurite.thimble.example.item.WandItem;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * The entry-point for this example mod. This is where you will likely
 * want to register your {@link dev.lazurite.thimble.composition.Composition}
 * objects. This is also where we register the {@link WandItem}.
 */
public class ServerInitializer implements ModInitializer {
    public static final String MODID = "examplemod";

    /**
     * Our test item for testing the example {@link dev.lazurite.thimble.composition.Composition} objects.
     */
    public static WandItem WAND_ITEM;

    /*
     * Register each composition. Make sure to do this statically
     * and not inside of a method like onInitialize().
     */
    static {
        Thimble.register(FloatAwayComposition::new);
        Thimble.register(TimeBombComposition::new);
    }

    /**
     * Registers the {@link WandItem}.
     */
    @Override
    public void onInitialize() {
        WAND_ITEM = Registry.register(
                Registry.ITEM,
                new Identifier(MODID, "wand_item"),
                new WandItem(new Item.Settings().maxCount(1).group(ItemGroup.MISC))
        );
    }
}

