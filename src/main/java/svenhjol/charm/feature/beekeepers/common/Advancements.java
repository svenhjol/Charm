package svenhjol.charm.feature.beekeepers.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.beekeepers.Beekeepers;
import svenhjol.charm.foundation.feature.AdvancementHolder;

public final class Advancements extends AdvancementHolder<Beekeepers> {
    public Advancements(Beekeepers feature) {
        super(feature);
    }

    public void tradedWithBeekeeper(Player player) {
        trigger("traded_with_beekeeper", player);
    }
}
