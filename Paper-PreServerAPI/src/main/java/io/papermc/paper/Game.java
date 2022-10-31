package io.papermc.paper;

import io.papermc.paper.plugin.PluginSystem;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface Game {
    @NotNull PluginSystem pluginSystem();

    Optional<Server> server();
}
