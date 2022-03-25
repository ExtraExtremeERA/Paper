package io.papermc.paper.testplugin.behaviors.squid;

import io.papermc.paper.entity.brain.BrainHolder;
import io.papermc.paper.entity.brain.activity.behavior.Behavior;
import io.papermc.paper.entity.brain.memory.MemoryKeyStatus;
import io.papermc.paper.entity.brain.memory.MemoryPair;
import io.papermc.paper.testplugin.TestPlugin;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Bukkit;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Squid;

import java.util.Collection;
import java.util.List;

public class HuntSquidsBehavior implements Behavior<Mob> {

    private Squid target;

    @Override
    public void start(Mob entity) {
        entity.playSound(Sound.sound(org.bukkit.Sound.ENTITY_WOLF_GROWL, Sound.Source.HOSTILE, 5, 1F));
    }

    @Override
    public void tick(Mob entity) {
        List<Squid> squids = entity.getMemory(TestPlugin.SQUID_CANDIDATES);
        if (target == null || target.isDead()) {
            if (squids.isEmpty()) {
                if (entity instanceof BrainHolder<?> holder) {
                    holder.forgetMemory(TestPlugin.SQUID_CANDIDATES);
                    holder.forgetMemory(TestPlugin.SQUID_RAGE);
                }
                return;
            }

            target = squids.remove(0);
        } else {
            entity.getPathfinder().moveTo(target);
        }

        if (entity.getLocation().distanceSquared(target.getLocation()) < 2) {
            target.damage(9999, entity);
            entity.playSound(Sound.sound(org.bukkit.Sound.ENTITY_WOLF_DEATH, Sound.Source.HOSTILE, 5, 1F));
        }
    }

    @Override
    public void stop(Mob entity) {
        entity.getPathfinder().stopPathfinding();
        entity.playSound(Sound.sound(org.bukkit.Sound.ENTITY_WOLF_WHINE, Sound.Source.HOSTILE, 5, 1F));
    }

    @Override
    public boolean canStillRun(Mob entity) {
        return entity.getMemory(TestPlugin.SQUID_RAGE) != null && entity.getMemory(TestPlugin.SQUID_CANDIDATES) != null && !entity.getMemory(TestPlugin.SQUID_CANDIDATES).isEmpty();
    }

    @Override
    public int getMinRuntime() {
        return 20;
    }

    @Override
    public int getMaxRuntime() {
        return 100;
    }

    @Override
    public Collection<MemoryPair> getMemoryRequirements() {
        return List.of(new MemoryPair(MemoryKeyStatus.PRESENT, TestPlugin.SQUID_RAGE), new MemoryPair(MemoryKeyStatus.PRESENT, TestPlugin.SQUID_CANDIDATES));
    }
}
