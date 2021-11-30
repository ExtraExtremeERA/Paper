package io.papermc.paper.testplugin.behaviors;

import io.papermc.paper.entity.brain.activity.behavior.Behavior;
import io.papermc.paper.entity.brain.memory.MemoryPair;
import org.bukkit.entity.LivingEntity;

import java.util.Collection;
import java.util.List;

public class SpinBehavior implements Behavior<LivingEntity> {

    @Override
    public void start(LivingEntity entity) {
        entity.setJumping(true);
    }

    @Override
    public void tick(LivingEntity entity) {
        entity.setRotation((float) (Math.random() * 180F), (float) (Math.random() * 180F));
    }

    @Override
    public void stop(LivingEntity entity) {
        entity.setJumping(false);
    }

    @Override
    public int getMinRuntime() {
        return 10;
    }

    @Override
    public int getMaxRuntime() {
        return 50;
    }

    @Override
    public Collection<MemoryPair> getMemoryRequirements() {
        return List.of();
    }
}
