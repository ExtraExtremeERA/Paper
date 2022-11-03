package io.papermc.paper;

import io.papermc.paper.plugin.PluginSystem;
import io.papermc.paper.registry.RegistryDebugging;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * A Game interface, the idea is ~~stolen~~ from Sponge.
 */
public interface Game {
    @NotNull RegistryDebugging registryDebugging();

    @NotNull PluginSystem pluginSystem();

    Optional<Server> server();
}
