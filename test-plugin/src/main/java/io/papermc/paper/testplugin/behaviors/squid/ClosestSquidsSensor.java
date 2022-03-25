package io.papermc.paper.testplugin.behaviors.squid;

import io.papermc.paper.entity.brain.sensor.Sensor;
import io.papermc.paper.testplugin.TestPlugin;
import org.bukkit.entity.Goat;
import org.bukkit.entity.Squid;
import org.bukkit.entity.memory.MemoryKey;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class ClosestSquidsSensor implements Sensor<Goat> {

    @Override
    public int getInterval() {
        return 10;
    }

    @Override
    public void tick(@NotNull Goat entity) {
        List<Squid> squids = entity.getNearbyEntities(10, 5, 10)
            .stream()
            .filter((nearbyEntity) -> nearbyEntity instanceof Squid)
            .map((squid) -> ((Squid) squid))
            .toList();

        if (!squids.isEmpty()) {
            entity.setMemory(TestPlugin.SQUID_CANDIDATES, new ArrayList<>(squids));
        } else {
            entity.forgetMemory(TestPlugin.SQUID_CANDIDATES);
        }
    }

    @Override
    public @NotNull Collection<MemoryKey<?>> requiredMemories() {
        return List.of(TestPlugin.SQUID_CANDIDATES);
    }
}
