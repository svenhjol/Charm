package svenhjol.charm.feature.beekeepers.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.beekeepers.Beekeepers;
import svenhjol.charm.feature.core.custom_advancements.common.AdvancementHolder;

public final class Advancements extends AdvancementHolder<Beekeepers> {
    public Advancements(Beekeepers feature) {
        super(feature);
    }

    public void tradedWithBeekeeper(Player player) {
        trigger("traded_with_beekeeper", player);
    }
}
