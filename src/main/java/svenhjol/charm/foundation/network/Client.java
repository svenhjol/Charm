package svenhjol.charm.foundation.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

public class Client implements Network {
    private static Client instance;

    public static Client instance() {
        if (instance == null) {
            instance = new Client();
        }
        return instance;
    }

    @Override
    public void send(Player player, CustomPacketPayload payload) {
        ClientPlayNetworking.send(payload);
    }
}
