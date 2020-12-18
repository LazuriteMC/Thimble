package dev.lazurite.thimble.example.composition;

import dev.lazurite.thimble.composition.Composition;
import dev.lazurite.thimble.example.ServerInitializer;
import dev.lazurite.thimble.synchronizer.Synchronizer;
import dev.lazurite.thimble.synchronizer.key.SynchronizedKey;
import dev.lazurite.thimble.synchronizer.type.SynchronizedTypeRegistry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

/**
 * This {@link Composition} causes any associated {@link Entity} to emit smoke
 * and eventually explode after a certain amount of time.
 * @author Ethan Johnson
 */
public class TimeBombComposition extends Composition {
    /**
     * The identifier which allows the game to distinguish it when serialized
     * within a packet or a {@link net.minecraft.nbt.CompoundTag}.
     */
    public static final Identifier IDENTIFIER = new Identifier(ServerInitializer.MODID, "time_bomb");

    /**
     * A synchronized value, representing whether or not the time bomb is armed.
     */
    public static final SynchronizedKey<Boolean> ARMED = Synchronizer.register(new Identifier(ServerInitializer.MODID, "armed"), SynchronizedTypeRegistry.BOOLEAN, true);

    /**
     * A synchronized value, representing how much time is left before the explosion :)
     * (measured in ticks, starting value is 100)
     */
    public static final SynchronizedKey<Integer> TIMER = Synchronizer.register(new Identifier(ServerInitializer.MODID, "timer"), SynchronizedTypeRegistry.INTEGER, 100);

    /**
     * Default constructor, necessary in order to register the {@link Composition}.
     * @param synchronizer the {@link Synchronizer} that it will be using
     */
    public TimeBombComposition(Synchronizer synchronizer) {
        super(synchronizer);
        getSynchronizer().set(TIMER, 60);
    }

    /**
     * The main spot where you'll define your custom behavior. All this
     * does is set the upwards velocity of the {@link Entity} to the rate.
     * @param entity the {@link Entity} with this {@link Composition} attached
     */
    @Override
    public void onTick(Entity entity) {
        World world = entity.getEntityWorld();
        int timeLeft = getSynchronizer().get(TIMER);

        if (getSynchronizer().get(ARMED)) {
            if (world.isClient()) {
                ClientWorld clientWorld = (ClientWorld) world;
                clientWorld.addParticle(ParticleTypes.LARGE_SMOKE, true, entity.getX(), entity.getY(), entity.getZ(), 0, 0, 0);
            } else {
                if (timeLeft <= 0) {
                    entity.getEntityWorld().createExplosion(null, entity.getX(), entity.getBodyY(0.0625D), entity.getZ(), 8.0F, Explosion.DestructionType.BREAK);
                    getSynchronizer().set(ARMED, false);
                } else {
                    getSynchronizer().set(TIMER, timeLeft - 1);
                }
            }
        }
    }

    /**
     * Sets the time bomb to be disarmed.
     * @param entity the {@link Entity} who has the {@link Composition} stitched
     * @param player the {@link PlayerEntity} who is interacting
     * @param hand the {@link Hand} of the {@link PlayerEntity}
     * @return whether or not the player should swing their hand
     */
    @Override
    public boolean onInteract(Entity entity, PlayerEntity player, Hand hand) {
        if (player.getEntityWorld().isClient()) {
            getSynchronizer().set(ARMED, false);
        }

        return true;
    }

    /**
     * Called when the {@link Entity} is
     * removed from the {@link World}.
     * @param entity the {@link Entity} who has the {@link Composition} stitched
     */
    @Override
    public void onRemove(Entity entity) {

    }

    /**
     * This is where any synchronized values are set up to be tracked
     * by the {@link dev.lazurite.thimble.synchronizer.Synchronizer}.
     */
    @Override
    public void initSynchronizer() {
        getSynchronizer().track(ARMED);
        getSynchronizer().track(TIMER);
    }

    /**
     * Gets the {@link Identifier} for this {@link Composition}.
     * @return the {@link Identifier}
     */
    @Override
    public Identifier getIdentifier() {
        return IDENTIFIER;
    }
}
