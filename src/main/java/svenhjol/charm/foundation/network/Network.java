package svenhjol.charm.foundation.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

public interface Network {
    void send(Player player, CustomPacketPayload payload);
}
