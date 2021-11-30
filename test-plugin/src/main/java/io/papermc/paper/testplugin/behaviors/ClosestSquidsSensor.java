package io.papermc.paper.testplugin.behaviors;

import io.papermc.paper.entity.brain.BrainHolder;
import io.papermc.paper.entity.brain.sensor.Sensor;
import io.papermc.paper.testplugin.TestPlugin;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Squid;
import org.bukkit.entity.memory.MemoryKey;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class ClosestSquidsSensor implements Sensor<LivingEntity> {

    @Override
    public int getInterval() {
        return 10;
    }

    @Override
    public void tick(@NotNull LivingEntity entity) {
        List<Squid> squids = entity.getNearbyEntities(10, 5, 10)
            .stream()
            .filter((nearbyEntity) -> nearbyEntity instanceof Squid)
            .map((squid) -> ((Squid) squid))
            .toList();

        var brainHolder = ((BrainHolder<?>) entity);

        if (!squids.isEmpty()) {
            brainHolder.setMemory(TestPlugin.SQUID_CANDIDATES, new ArrayList<>(squids), Long.MAX_VALUE);
        } else {
            brainHolder.forgetMemory(TestPlugin.SQUID_CANDIDATES);
        }

        if (squids.size() == 3) { // Once 3 squids are found
            brainHolder.setMemory(TestPlugin.SQUID_RAGE, true, 200); // angry for 10 secs
        }
    }

    @Override
    public @NotNull Collection<MemoryKey<?>> requiredMemories() {
        return List.of(TestPlugin.SQUID_CANDIDATES, TestPlugin.SQUID_RAGE);
    }
}
