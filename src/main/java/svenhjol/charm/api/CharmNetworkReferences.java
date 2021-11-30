package svenhjol.charm.api;

import net.minecraft.util.StringRepresentable;

/**
 * @version 4.0.0
 */
@SuppressWarnings("unused")
public enum CharmNetworkReferences implements StringRepresentable {
    ClientUpdatePlayerState("charm:client_update_player_state"),
    ServerUpdatePlayerState("charm:server_update_player_state");

    private final String name;

    CharmNetworkReferences(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
