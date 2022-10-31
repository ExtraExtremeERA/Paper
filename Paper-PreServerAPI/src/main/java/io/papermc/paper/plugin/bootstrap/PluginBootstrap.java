package io.papermc.paper.plugin.bootstrap;

import io.papermc.paper.plugin.provider.util.ProviderUtil;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * A plugin boostrap is meant for loading certain parts of the plugin before the server is loaded.
 * <p>
 * Plugin bootstrapping allows values to be initialized in certain parts of the server that might not be allowed
 * when the server is running.
 * <p>
 * Your bootstrap class will be on the same classloader as your JavaPlugin.
 * <p>
 * <b>All calls to Bukkit may throw a NullPointerExceptions or return null unexpectedly. You should only call api methods that are explicitly documented to work in the bootstrapper</b>
 */
@ApiStatus.OverrideOnly
public interface PluginBootstrap {

    /**
     * Called by the server, allowing you to bootstrap with context that provides things like a logger and your shared plugin configuration file.
     *
     * @param context server provided context
     */
    void bootstrap(@NotNull PluginBootstrapContext context);

    /**
     * Called by the server, allows you to create your own java plugin instance inorder to do things such as
     * pass objects through the constructor.
     * @param context server created bootstrap object
     * @return java plugin instance
     */
    @NotNull
    default JavaPlugin createPlugin(@NotNull PluginBootstrapContext context) {
        return ProviderUtil.loadClass(context.getConfiguration().getMain(), JavaPlugin.class, this.getClass().getClassLoader());
    }
}
