package svenhjol.charm.feature.lumberjacks.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.lumberjacks.Lumberjacks;
import svenhjol.charm.foundation.feature.AdvancementHolder;

public final class Advancements extends AdvancementHolder<Lumberjacks> {
    public Advancements(Lumberjacks feature) {
        super(feature);
    }

    public void tradedWithLumberjack(Player player) {
        trigger("traded_with_lumberjack", player);
    }
}
