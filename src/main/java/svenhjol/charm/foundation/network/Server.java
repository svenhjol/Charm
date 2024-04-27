package svenhjol.charm.foundation.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class Server implements Network {
    private static Server instance;

    public static Server instance() {
        if (instance == null) {
            instance = new Server();
        }
        return instance;
    }

    @Override
    public void send(Player player, CustomPacketPayload payload) {
        ServerPlayNetworking.send((ServerPlayer)player, payload);
    }
}
