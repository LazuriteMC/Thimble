package dev.lazurite.thimble.example.composition;

import dev.lazurite.thimble.composition.Composition;
import dev.lazurite.thimble.example.ServerInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ParticleComposition extends Composition {
    public static final Identifier identifier = new Identifier(ServerInitializer.MODID, "particle");

    public ParticleComposition() {

    }

    @Override
    public void tick(Entity entity) {
        World world = entity.getEntityWorld();

        if (world.isClient()) {
            WorldRenderer worldRenderer = MinecraftClient.getInstance().worldRenderer;
            worldRenderer.addParticle(new DustParticleEffect(1.0f, 1.0f, 0.0f, 1.0f), true, entity.getX(), entity.getY(), entity.getZ(), 0, 0, 0);
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
