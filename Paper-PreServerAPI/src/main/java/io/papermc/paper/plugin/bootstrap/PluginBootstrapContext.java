package io.papermc.paper.plugin.bootstrap;

import io.papermc.paper.Game;
import io.papermc.paper.plugin.configuration.PluginConfiguration;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.logging.Logger;

/**
 * Contains context used for when a plugin's bootstrap class is executed.
 */
@ApiStatus.NonExtendable
public interface PluginBootstrapContext {

    @NotNull
    Game getGame();

    /**
     * Gets the plugin's configuration.
     * @return configuration
     */
    @NotNull
    PluginConfiguration getConfiguration();

    /**
     * Gets the path to the data directory for the plugin.
     *
     * @return plugin
     */
    @NotNull
    Path getDataDirectory();

    /**
     * Gets the configuration file for this plugin.
     *
     * @return configuration file
     */
    @NotNull
    Path getConfigurationFile();

    /**
     * Gets the logger used for this plugin.
     *
     * @return logger
     */
    @NotNull
    Logger getLogger();
}
