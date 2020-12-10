package dev.lazurite.thimble.example.composition;

import dev.lazurite.thimble.composition.Composition;
import dev.lazurite.thimble.example.ServerInitializer;
import dev.lazurite.thimble.synchronizer.key.SynchronizedKey;
import dev.lazurite.thimble.synchronizer.key.SynchronizedKeyRegistry;
import dev.lazurite.thimble.synchronizer.type.SynchronizedTypeRegistry;
import dev.lazurite.thimble.util.TickTimer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class FloatAwayComposition extends Composition {
    public static final SynchronizedKey<Float> RATE = SynchronizedKeyRegistry.register(new Identifier(ServerInitializer.MODID, "rate"), SynchronizedTypeRegistry.FLOAT, 0.05f);
    public static final Identifier identifier = new Identifier(ServerInitializer.MODID, "float_away");

    private final TickTimer timer;

    public FloatAwayComposition() {
        timer = new TickTimer(60);
    }

    public FloatAwayComposition(float rate) {
        this();
        getSynchronizer().set(RATE, rate);
    }

    @Override
    public void tick(Entity entity) {
        World world = entity.getEntityWorld();

        System.out.println("RATE: " + getSynchronizer().get(RATE));

        if (world.isClient()) {

        } else {
            floatUp(entity, getSynchronizer().get(RATE));

            if (timer.tick()) {
//                Random random = new Random();
//                getSynchronizer().set(RATE, (random.nextInt(70)+1) / 100.0f);
            }
        }
    }

    public void floatUp(Entity entity, float rate) {
        entity.setVelocity(0, rate, 0);
    }

    @Override
    public void initSynchronizer() {
        getSynchronizer().track(RATE);
    }

    @Override
    public Identifier getIdentifier() {
        return identifier;
    }
}
