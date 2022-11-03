package io.papermc.paper.registry;

import org.jetbrains.annotations.ApiStatus;

// Remove later
@ApiStatus.Internal
public abstract class RegistryDebugging {

    public abstract void debugAddDimension();

    public abstract void debugAddChatType();

    public abstract void debugAddMemory();

    public abstract void finallyDebug();
}
