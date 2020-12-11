package dev.lazurite.thimble.example.composition;

import dev.lazurite.thimble.composition.Composition;
import dev.lazurite.thimble.example.ServerInitializer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class SmokeComposition extends Composition {
    public static final Identifier identifier = new Identifier(ServerInitializer.MODID, "particle");

    public SmokeComposition() {

    }

    @Override
    public void tick(Entity entity) {
        World world = entity.getEntityWorld();

        if (world.isClient()) {
            ClientWorld clientWorld = (ClientWorld) world;
            clientWorld.addParticle(ParticleTypes.LARGE_SMOKE, true, entity.getX(), entity.getY(), entity.getZ(), 0, 0, 0);
        }
    }

    @Override
    public void initSynchronizer() {

    }

    @Override
    public Identifier getIdentifier() {
        return identifier;
    }
}
