package dev.lazurite.thimble.example.composition;

import dev.lazurite.thimble.composition.Composition;
import dev.lazurite.thimble.example.ServerInitializer;
import dev.lazurite.thimble.synchronizer.Synchronizer;
import dev.lazurite.thimble.synchronizer.key.SynchronizedKey;
import dev.lazurite.thimble.synchronizer.type.SynchronizedTypeRegistry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

/**
 * This {@link Composition} causes any associated {@link Entity} to emit smoke particles.
 * @author Ethan Johnson
 */
public class SmokeComposition extends Composition {
    /**
     * The identifier which allows the game to distinguish it when serialized
     * within a packet or a {@link net.minecraft.nbt.CompoundTag}.
     */
    public static final Identifier identifier = new Identifier(ServerInitializer.MODID, "smoke");

    /**
     * A synchronized value, representing whether or
     * not the {@link Entity} should emit smoke.
     */
    public static final SynchronizedKey<Boolean> SHOULD_SMOKE = Synchronizer.register(new Identifier(ServerInitializer.MODID, "should_smoke"), SynchronizedTypeRegistry.BOOLEAN, true);

    /**
     * Default constructor, necessary in order to register the {@link Composition}.
     */
    public SmokeComposition(Synchronizer synchronizer) {
        super(synchronizer);
    }

    /**
     * The main spot where you'll define your custom behavior. All this
     * does is emit smoke particles on the client-side of the game.
     * @param entity the {@link Entity} with this {@link Composition} attached
     */
    @Override
    public void tick(Entity entity) {
        World world = entity.getEntityWorld();

        /* Only do this sort of thing on the client */
        if (world.isClient() && getSynchronizer().get(SHOULD_SMOKE)) {
            ClientWorld clientWorld = (ClientWorld) world;
            clientWorld.addParticle(ParticleTypes.LARGE_SMOKE, true, entity.getX(), entity.getY(), entity.getZ(), 0, 0, 0);
        }
    }

    /**
     * Sets the {@link Entity} to not smoke.
     * @param player the {@link PlayerEntity} who is interacting
     * @param hand the {@link Hand} of the {@link PlayerEntity}
     */
    @Override
    public void interact(PlayerEntity player, Hand hand) {
        getSynchronizer().set(SHOULD_SMOKE, false);
    }

    /**
     * Sets the {@link Entity} to smoke.
     * @param source the source of damage
     * @param amount the amount of damage taken
     */
    @Override
    public void damage(DamageSource source, float amount) {
        getSynchronizer().set(SHOULD_SMOKE, true);
    }

    /**
     * Called when the {@link Entity} is
     * removed from the {@link World}.
     */
    @Override
    public void remove() {

    }

    /**
     * This only contains one synchronized value.
     */
    @Override
    public void initSynchronizer() {
        getSynchronizer().track(SHOULD_SMOKE);
    }

    @Override
    public Identifier getIdentifier() {
        return identifier;
    }
}
