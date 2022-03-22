package io.papermc.paper.testplugin.behaviors;

import io.papermc.paper.entity.brain.activity.behavior.Behavior;
import io.papermc.paper.entity.brain.memory.MemoryTypeStatus;
import io.papermc.paper.entity.brain.memory.MemoryPair;
import io.papermc.paper.testplugin.TestPlugin;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.Mob;

import java.util.Collection;
import java.util.List;

public class ScreamAtParrotsBehavior implements Behavior<Mob> {

    @Override
    public void start(Mob entity) {
        entity.playSound(Sound.sound(org.bukkit.Sound.ENTITY_LLAMA_SPIT, Sound.Source.HOSTILE, 5, 0.1F));
        entity.setGlowing(true);
    }

    @Override
    public void tick(Mob entity) {
        entity.playSound(Sound.sound(org.bukkit.Sound.ENTITY_GOAT_SCREAMING_PREPARE_RAM, Sound.Source.HOSTILE, 5, 1F));
    }

    @Override
    public void stop(Mob entity) {
        entity.setGlowing(false);
    }

    @Override
    public int getMinRuntime() {
        return 20;
    }

    @Override
    public int getMaxRuntime() {
        return 50;
    }

    @Override
    public Collection<MemoryPair> getMemoryRequirements() {
        return List.of(new MemoryPair(MemoryTypeStatus.PRESENT, TestPlugin.NEARBY_PARROTS));
    }
}
