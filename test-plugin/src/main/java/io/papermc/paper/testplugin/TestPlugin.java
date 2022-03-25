package io.papermc.paper.testplugin;

import io.leangen.geantyref.TypeToken;
import io.papermc.paper.entity.brain.activity.VanillaActivityKey;
import io.papermc.paper.entity.brain.sensor.SensorKey;
import io.papermc.paper.testplugin.behaviors.SniffSquidsBehavior;
import io.papermc.paper.testplugin.behaviors.SpinBehavior;
import io.papermc.paper.testplugin.behaviors.item.NearbyItemsSensor;
import io.papermc.paper.testplugin.behaviors.item.PickupItemsBehavior;
import io.papermc.paper.testplugin.behaviors.squid.AngryAtSquidBehavior;
import io.papermc.paper.testplugin.behaviors.squid.ClosestSquidsSensor;
import io.papermc.paper.testplugin.behaviors.squid.HuntSquidsBehavior;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Goat;
import org.bukkit.entity.Item;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Squid;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.List;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    public static final MemoryKey<List<Squid>> SQUID_CANDIDATES = Bukkit.registerMemoryKey(NamespacedKey.fromString("myplugin:squid_candidates"), new TypeToken<>() {
    }, null);
    public static final MemoryKey<List<Parrot>> NEARBY_PARROTS = Bukkit.registerMemoryKey(NamespacedKey.fromString("myplugin:nearby_parrots"), new TypeToken<>() {
    }, null);

    public static final MemoryKey<Boolean> SQUID_RAGE = Bukkit.registerMemoryKey(NamespacedKey.fromString("myplugin:squid_rage"), TypeToken.get(boolean.class), null);

    public static final MemoryKey<Integer> PICKED_ITEMS = Bukkit.registerMemoryKey(NamespacedKey.fromString("myplugin:picked_items"), TypeToken.get(int.class), PersistentDataType.INTEGER);
    public static final MemoryKey<List<Item>> NEARBY_ITEMS = Bukkit.registerMemoryKey(NamespacedKey.fromString("myplugin:nearby_items"), new TypeToken<>() {
    }, null);


    private final SensorKey SNIFF_SQUID_SENSOR = Bukkit.createSensorKey(NamespacedKey.fromString("scary_mobs_finder", this));
    private final SensorKey PARROT_SENSOR = Bukkit.createSensorKey(NamespacedKey.fromString("parrot_finder", this));
    private final SensorKey ITEM_SNIFFER = Bukkit.createSensorKey(NamespacedKey.fromString("cake_sniffer", this));

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Goat goat) {


            System.out.println("Activities: ");
            for (var activities : goat.getPrioritizedActivities().entrySet()) {
                debug("Priority: " + activities.getKey(), activities.getValue());
            }
            debug("Core Activities", goat.getCoreActivities());
            debug("Active Activities", goat.getActiveActivities());
            System.out.println("Sensors: " + goat.getSensors());
            debug("Memories", goat.getMemories());

            if (event.getPlayer().isSneaking()) {
                return;
            }

            // Clear vanilla stuff
            goat.clearActivities();
            goat.unregisterMemories();
            goat.clearSensors();

            goat.registerMemory(SQUID_CANDIDATES); // Register the custom memory
            goat.registerMemory(SQUID_RAGE);
            goat.registerMemory(PICKED_ITEMS);
            goat.registerMemory(NEARBY_ITEMS);

            goat.addActivity(VanillaActivityKey.IDLE, 1, List.of(new SpinBehavior(), new SniffSquidsBehavior()));
            goat.addActivity(VanillaActivityKey.CORE, 1, List.of(new AngryAtSquidBehavior(), new HuntSquidsBehavior(), new PickupItemsBehavior()));

            goat.addSensor(SNIFF_SQUID_SENSOR, new ClosestSquidsSensor()); // Add the scary mob finder sensor
            goat.addSensor(ITEM_SNIFFER, new NearbyItemsSensor());

            goat.setDefaultActivity(VanillaActivityKey.IDLE); // If no activities at this moment can activate, it goes to default.
            goat.setCoreActivities(List.of(VanillaActivityKey.CORE)); // This activity is ALWAYS active during other activities

            goat.setMemory(TestPlugin.PICKED_ITEMS, 0);
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
