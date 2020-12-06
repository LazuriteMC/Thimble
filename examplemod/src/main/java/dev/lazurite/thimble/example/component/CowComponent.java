package dev.lazurite.thimble.example.component;

import dev.lazurite.thimble.component.UniqueComponent;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.world.World;

public class CowComponent extends UniqueComponent<CowEntity> {

    public CowComponent(CowEntity owner) {
        super(owner);
    }

    @Override
    public void tick() {
        World world = getOwner().getEntityWorld();

        System.out.println("TEST PRINT");

        if (!world.isClient()) {
            floatAway();
        }
    }

    public void floatAway() {
        getOwner().setVelocity(0, 0.1, 0);
    }

    @Override
    public void initSynchronizer() {

    }
}
