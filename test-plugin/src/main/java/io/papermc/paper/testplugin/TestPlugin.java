package io.papermc.paper.testplugin;

import io.papermc.paper.entity.brain.BrainManager;
import io.papermc.paper.entity.brain.activity.VanillaActivityKey;
import io.papermc.paper.entity.brain.sensor.SensorKey;
import io.papermc.paper.testplugin.behaviors.ClosestParrotSensor;
import io.papermc.paper.testplugin.behaviors.ClosestSquidsSensor;
import io.papermc.paper.testplugin.behaviors.HuntSquidsBehavior;
import io.papermc.paper.testplugin.behaviors.ScreamAtParrotsBehavior;
import io.papermc.paper.testplugin.behaviors.SniffSquidsBehavior;
import io.papermc.paper.testplugin.behaviors.SpinBehavior;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Goat;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Squid;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.List;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    public static final MemoryKey<List<Squid>> SQUID_CANDIDATES = Bukkit.createMemoryKey(NamespacedKey.fromString("squid_candidates"));
    public static final MemoryKey<List<Parrot>> NEARBY_PARROTS = Bukkit.createMemoryKey(NamespacedKey.fromString("nearby_parrots"));

    public static final MemoryKey<Boolean> SQUID_RAGE = Bukkit.createMemoryKey(NamespacedKey.fromString("squid_rage"));

    private final SensorKey SNIFF_SQUID_SENSOR = Bukkit.createSensorKey(NamespacedKey.fromString("scary_mobs_finder", this));
    private final SensorKey PARROT_SENSOR = Bukkit.createSensorKey(NamespacedKey.fromString("parrot_finder", this));

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Goat brainHolder) {
            BrainManager manager = Bukkit.getBrainManager();
            System.out.println("Activities: ");
            for (var activities : manager.getPrioritizedActivities(brainHolder).entrySet()) {
                debug("Priority: " + activities.getKey(), activities.getValue());
            }
            debug("Core Activities", manager.getCoreActivities(brainHolder));
            debug("Active Activities", manager.getActiveActivities(brainHolder));
            System.out.println("Sensors: " + manager.getSensors(brainHolder));
            debug("Memories", manager.getMemories(brainHolder));

            if (event.getPlayer().isSneaking()) {
                return;
            }

            // Clear vanilla stuff
            manager.clearActivities(brainHolder);
            manager.unregisterMemories(brainHolder);
            manager.clearSensors(brainHolder);

            manager.registerMemory(brainHolder, SQUID_CANDIDATES); // Register the custom memory
            manager.registerMemory(brainHolder, SQUID_RAGE);

            manager.addActivity(brainHolder, VanillaActivityKey.IDLE, 1, List.of(new HuntSquidsBehavior(), new SpinBehavior()));
            manager.addActivity(brainHolder, VanillaActivityKey.CORE, 1, List.of(new SniffSquidsBehavior(), new ScreamAtParrotsBehavior()));

            manager.addSensor(brainHolder, SNIFF_SQUID_SENSOR, new ClosestSquidsSensor()); // Add the scary mob finder sensor
            manager.addSensor(brainHolder, PARROT_SENSOR, new ClosestParrotSensor());

            manager.setDefaultActivity(brainHolder, VanillaActivityKey.IDLE); // If no activities at this moment can activate, it goes to default.
            manager.setCoreActivities(brainHolder, List.of(VanillaActivityKey.CORE)); // This activity is ALWAYS active during other activities
        }

    }

    private static void debug(String name, Collection<? extends Keyed> collection) {
        System.out.println(name + ":");
        for (Keyed keyed : collection) {
            if (keyed == null) {
                continue;
            }
            System.out.println(keyed.getKey());
        }
    }
}
