package svenhjol.charm.feature.kilns.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.kilns.Kilns;
import svenhjol.charm.feature.core.custom_advancements.common.AdvancementHolder;

public final class Advancements extends AdvancementHolder<Kilns> {
    public Advancements(Kilns feature) {
        super(feature);
    }

    public void firedItem(Player player) {
        trigger("fired_item", player);
    }
}
