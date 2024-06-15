package svenhjol.charm.charmony.iface;

import net.minecraft.world.entity.player.Player;

@FunctionalInterface
public interface PacketHandler<T extends PacketRequest> {
    void handle(T var1, Player var2);
}
