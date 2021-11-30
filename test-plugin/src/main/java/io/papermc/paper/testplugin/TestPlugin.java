package io.papermc.paper.testplugin;

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

            System.out.println("Activities: ");
            for (var activities : brainHolder.getPrioritizedActivities().entrySet()) {
                debug("Priority: " + activities.getKey(), activities.getValue());
            }
            debug("Core Activities", brainHolder.getCoreActivities());
            debug("Active Activities", brainHolder.getActiveActivities());
            System.out.println("Sensors: " + brainHolder.getSensors());
            debug("Memories", brainHolder.getMemories());

            if (event.getPlayer().isSneaking()) {
                return;
            }

            // Clear vanilla stuff
            brainHolder.clearActivities();
            brainHolder.unregisterMemories();
            brainHolder.clearSensors();

            brainHolder.registerMemory(SQUID_CANDIDATES); // Register the custom memory
            brainHolder.registerMemory(SQUID_RAGE);

            brainHolder.addActivity(VanillaActivityKey.IDLE, 1, List.of(new HuntSquidsBehavior(), new SpinBehavior()));
            brainHolder.addActivity(VanillaActivityKey.CORE, 1, List.of(new SniffSquidsBehavior(), new ScreamAtParrotsBehavior()));

            brainHolder.addSensor(SNIFF_SQUID_SENSOR, new ClosestSquidsSensor()); // Add the scary mob finder sensor
            brainHolder.addSensor(PARROT_SENSOR, new ClosestParrotSensor());

            brainHolder.setDefaultActivity(VanillaActivityKey.IDLE); // If no activities at this moment can activate, it goes to default.
            brainHolder.setCoreActivities(List.of(VanillaActivityKey.CORE)); // This activity is ALWAYS active during other activities
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
