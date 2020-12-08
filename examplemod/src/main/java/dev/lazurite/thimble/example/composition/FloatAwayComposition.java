package dev.lazurite.thimble.example.composition;

import dev.lazurite.thimble.composition.Composition;
import dev.lazurite.thimble.synchronizer.SynchronizedKey;
import dev.lazurite.thimble.synchronizer.type.SynchronizedTypeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class FloatAwayComposition extends Composition {
    public static final SynchronizedKey<Float> RATE = new SynchronizedKey<>(SynchronizedTypeRegistry.FLOAT, 0.05f);

    public FloatAwayComposition() {

    }

    public FloatAwayComposition(float rate) {
        getSynchronizer().set(RATE, rate);
    }

    @Override
    public void tick(Entity entity) {
        World world = entity.getEntityWorld();

        System.out.println("TEST PRINT: " + getSynchronizer().get(RATE));

        if (!world.isClient()) {
            floatAway(entity, getSynchronizer().get(RATE));
        }
    }

    public void floatAway(Entity entity, float rate) {
        entity.setVelocity(0, rate, 0);
    }

    @Override
    public void initSynchronizer() {
        getSynchronizer().track(RATE);
    }
}
