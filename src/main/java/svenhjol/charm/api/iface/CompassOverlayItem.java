package svenhjol.charm.api.iface;

import net.minecraft.world.entity.player.Player;

public interface CompassOverlayItem {
    boolean shouldShow(Player player);

    String text();

    int color();
}
