package dev.lazurite.thimble.example.composition;

import dev.lazurite.thimble.composition.Composition;
import dev.lazurite.thimble.example.ServerInitializer;
import dev.lazurite.thimble.synchronizer.Synchronizer;
import dev.lazurite.thimble.synchronizer.key.SynchronizedKey;
import dev.lazurite.thimble.synchronizer.type.SynchronizedTypeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

/**
 * This {@link Composition} causes any associated {@link Entity} to float upwards forever.
 * @author Ethan Johnson
 */
public class FloatAwayComposition extends Composition {
    /**
     * The identifier which allows the game to distinguish it when serialized
     * within a packet or a {@link net.minecraft.nbt.CompoundTag}.
     */
    public static final Identifier IDENTIFIER = new Identifier(ServerInitializer.MODID, "float_away");

    /**
     * A synchronized value, representing the rate at which an associated
     * {@link Entity} would float upwards.
     */
    public static final SynchronizedKey<Float> RATE = Synchronizer.register(new Identifier(ServerInitializer.MODID, "rate"), SynchronizedTypeRegistry.FLOAT, 0.1f);

    /**
     * A synchronized value, representing whether or not the
     * {@link Entity} should float away.
     */
    public static final SynchronizedKey<Boolean> SHOULD_FLOAT = Synchronizer.register(new Identifier(ServerInitializer.MODID, "should_float"), SynchronizedTypeRegistry.BOOLEAN, true);

    /**
     * Default constructor, necessary in order to register the {@link Composition}.
     * @param synchronizer the {@link Synchronizer} that it will be using
     */
    public FloatAwayComposition(Synchronizer synchronizer) {
        super(synchronizer);
    }

    /**
     * The main spot where you'll define your custom behavior. All this
     * does is set the upwards velocity of the {@link Entity} to the rate.
     * @param entity the {@link Entity} with this {@link Composition} attached
     */
    @Override
    public void onTick(Entity entity) {
        World world = entity.getEntityWorld();

        /* Only do this sort of thing on the server */
        if (!world.isClient()) {
            if (getSynchronizer().get(SHOULD_FLOAT)) {
                entity.setVelocity(0, getSynchronizer().get(RATE), 0);
            }
        }
    }

    /**
     * Sets the {@link Entity} speed to zero.
     * @param player the {@link PlayerEntity} who is interacting
     * @param hand the {@link Hand} of the {@link PlayerEntity}
     * @return whether or not the player should swing their hand
     */
    @Override
    public boolean onInteract(Entity entity, PlayerEntity player, Hand hand) {
        if (!player.getEntityWorld().isClient()) {
            getSynchronizer().set(SHOULD_FLOAT, !getSynchronizer().get(SHOULD_FLOAT));
        }

        return true;
    }

    /**
     * Called when the {@link Entity} is
     * removed from the {@link World}.
     */
    @Override
    public void onRemove(Entity entity) {

    }

    /**
     * This is where any synchronized values are
     * set up to be tracked by the {@link Synchronizer}.
     */
    @Override
    public void initSynchronizer() {
        getSynchronizer().track(RATE);
        getSynchronizer().track(SHOULD_FLOAT);
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
