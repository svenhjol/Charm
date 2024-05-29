package svenhjol.charm.feature.lumberjacks.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.core.custom_advancements.common.AdvancementHolder;
import svenhjol.charm.feature.lumberjacks.Lumberjacks;

public final class Advancements extends AdvancementHolder<Lumberjacks> {
    public Advancements(Lumberjacks feature) {
        super(feature);
    }

    public void tradedWithLumberjack(Player player) {
        trigger("traded_with_lumberjack", player);
    }
}
