package io.papermc.paper.testplugin.behaviors.squid;

import io.papermc.paper.entity.brain.activity.behavior.Behavior;
import io.papermc.paper.entity.brain.memory.MemoryKeyStatus;
import io.papermc.paper.entity.brain.memory.MemoryPair;
import io.papermc.paper.testplugin.TestPlugin;
import net.kyori.adventure.sound.Sound;
import org.bukkit.EntityEffect;
import org.bukkit.entity.Goat;
import org.bukkit.entity.Squid;
import org.bukkit.entity.memory.MemoryKey;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Goats HATE squids! GRRRR
 *
 * This causes it that if a goat has more than 3 squids nearby, it will get ANGRY at them!
 */
public class AngryAtSquidBehavior implements Behavior<Goat> {

    @Override
    public boolean canStart(Goat entity) {
        List<Squid> squids = entity.getMemory(TestPlugin.SQUID_CANDIDATES);

        return squids.size() >= 3; // Because the memory pair MUST be present this will never be null
    }

    @Override
    public void start(Goat entity) {
        entity.playSound(Sound.sound(org.bukkit.Sound.ENTITY_ENDERMAN_SCREAM, Sound.Source.MASTER, 1, 1), net.kyori.adventure.sound.Sound.Emitter.self());
        entity.setGlowing(true);

        entity.setMemory(TestPlugin.SQUID_RAGE, true, 200); // Make it angry for a little bit, it will cool down in 10 seconds
    }

    @Override
    public void tick(Goat entity) {
    }

    @Override
    public void stop(Goat entity) {
        entity.setGlowing(false);
    }

    @Override
    public int getMinRuntime() {
        return 100;
    }

    @Override
    public int getMaxRuntime() {
        return 200;
    }

    @Override
    public Collection<MemoryPair> getMemoryRequirements() {
        return List.of(new MemoryPair(MemoryKeyStatus.ABSENT, TestPlugin.SQUID_RAGE), new MemoryPair(MemoryKeyStatus.PRESENT, TestPlugin.SQUID_CANDIDATES));
    }
}
