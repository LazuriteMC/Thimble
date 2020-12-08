package dev.lazurite.thimble.example.composition;

import dev.lazurite.thimble.composition.Composition;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class FloatAwayComposition extends Composition {
    private float rate;

    public FloatAwayComposition(float rate) {
        super();
        this.rate = rate;
    }

    @Override
    public void tick(Entity entity) {
        World world = entity.getEntityWorld();

        System.out.println("TEST PRINT");

        if (!world.isClient()) {
            floatAway(entity, rate);
        }
    }

    public void floatAway(Entity entity, float rate) {
        entity.setVelocity(0, rate, 0);
    }

    @Override
    public void initSynchronizer() {

    }
}
