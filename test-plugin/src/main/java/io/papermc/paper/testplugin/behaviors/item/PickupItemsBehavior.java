package io.papermc.paper.testplugin.behaviors.item;

import io.papermc.paper.entity.brain.activity.behavior.Behavior;
import io.papermc.paper.entity.brain.memory.MemoryKeyStatus;
import io.papermc.paper.entity.brain.memory.MemoryPair;
import io.papermc.paper.testplugin.TestPlugin;
import org.bukkit.entity.Goat;
import org.bukkit.entity.Item;

import java.util.Collection;
import java.util.List;

public class PickupItemsBehavior implements Behavior<Goat> {

    @Override
    public boolean canStillRun(Goat entity) {
        return entity.getMemory(TestPlugin.NEARBY_ITEMS) != null;
    }

    @Override
    public void start(Goat entity) {
        entity.setBaby();
    }

    @Override
    public void tick(Goat entity) {
        List<Item> items = entity.getMemory(TestPlugin.NEARBY_ITEMS);
        Item item = items.get(0);
        entity.getPathfinder().moveTo(item.getLocation());

        if (item.getLocation().distance(entity.getLocation()) < 2) {
            entity.playPickupItemAnimation(item);
            item.remove();
            entity.setMemory(TestPlugin.PICKED_ITEMS, entity.getMemory(TestPlugin.PICKED_ITEMS) + 1);
        }
    }

    @Override
    public void stop(Goat entity) {
        entity.setAdult();
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
        return List.of(new MemoryPair(MemoryKeyStatus.PRESENT, TestPlugin.NEARBY_ITEMS), new MemoryPair(MemoryKeyStatus.PRESENT, TestPlugin.PICKED_ITEMS));
    }
}
