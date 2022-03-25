package io.papermc.paper.testplugin.behaviors.item;

import io.papermc.paper.entity.brain.sensor.Sensor;
import io.papermc.paper.testplugin.TestPlugin;
import org.bukkit.entity.Goat;
import org.bukkit.entity.Item;
import org.bukkit.entity.memory.MemoryKey;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class NearbyItemsSensor implements Sensor<Goat> {

    @Override
    public int getInterval() {
        return 10;
    }

    @Override
    public void tick(@NotNull Goat entity) {
        List<Item> items = entity.getNearbyEntities(10, 5, 10)
            .stream()
            .filter((nearbyEntity) -> nearbyEntity instanceof Item)
            .map((item) -> ((Item) item))
            .toList();

        if (!items.isEmpty()) {
            entity.setMemory(TestPlugin.NEARBY_ITEMS, new ArrayList<>(items));
        } else {
            entity.forgetMemory(TestPlugin.NEARBY_ITEMS);
        }
    }

    @Override
    public @NotNull Collection<MemoryKey<?>> requiredMemories() {
        return List.of(TestPlugin.NEARBY_ITEMS);
    }
}
