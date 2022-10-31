package io.papermc.paper;

import org.jetbrains.annotations.NotNull;

public final class PreServerAPI {
    private PreServerAPI() {}

    static Game game;

    @NotNull
    public static Game game() {
        return PreServerAPI.game;
    }
}
