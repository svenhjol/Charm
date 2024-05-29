package svenhjol.charm.feature.totem_of_preserving.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.totem_of_preserving.TotemOfPreserving;
import svenhjol.charm.feature.core.custom_advancements.common.AdvancementHolder;

public final class Advancements extends AdvancementHolder<TotemOfPreserving> {
    public Advancements(TotemOfPreserving feature) {
        super(feature);
    }

    public void usedTotem(Player player) {
        trigger("used_totem_of_preserving", player);
    }
}
