package svenhjol.charm.feature.casks.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.casks.Casks;
import svenhjol.charm.foundation.feature.AdvancementHolder;

public final class Advancements extends AdvancementHolder<Casks> {
    public Advancements(Casks feature) {
        super(feature);
    }

    public void addedLiquidToCask(Player player) {
        trigger("added_liquid_to_cask", player);
    }

    public void tookLiquidFromCask(Player player) {
        trigger("took_liquid_from_cask", player);
    }
}
