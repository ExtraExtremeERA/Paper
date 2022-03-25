package io.papermc.paper.testplugin.behaviors;

import io.papermc.paper.entity.brain.activity.behavior.Behavior;
import io.papermc.paper.entity.brain.memory.MemoryKeyStatus;
import io.papermc.paper.entity.brain.memory.MemoryPair;
import io.papermc.paper.testplugin.TestPlugin;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Particle;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Squid;

import java.util.Collection;
import java.util.List;

/**
 * When the goat isn't angry, it will sometimes want to sniff the nearby squids.
 */
public class SniffSquidsBehavior implements Behavior<Mob> {

    @Override
    public void start(Mob entity) {
        entity.playSound(Sound.sound(org.bukkit.Sound.ENTITY_LLAMA_SPIT, Sound.Source.HOSTILE, 5, 0.1F));
    }

    @Override
    public void tick(Mob entity) {
        List<Squid> squids = entity.getMemory(TestPlugin.SQUID_CANDIDATES);
        for (Squid squid : squids) {
            entity.lookAt(squid);
        }

        entity.getWorld().spawnParticle(Particle.HEART, entity.getEyeLocation(), 1);
    }

    @Override
    public void stop(Mob entity) {
        entity.getWorld().spawnParticle(Particle.SPIT, entity.getLocation(), 100, 10, 5, 10);
    }

    @Override
    public int getMinRuntime() {
        return 20;
    }

    @Override
    public int getMaxRuntime() {
        return 200;
    }

    @Override
    public Collection<MemoryPair> getMemoryRequirements() {
        return List.of(new MemoryPair(MemoryKeyStatus.PRESENT, TestPlugin.SQUID_CANDIDATES), new MemoryPair(MemoryKeyStatus.ABSENT, TestPlugin.SQUID_RAGE));
    }
}
