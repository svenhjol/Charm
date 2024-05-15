package svenhjol.charm.feature.endermite_powder.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.endermite_powder.EndermitePowder;
import svenhjol.charm.foundation.feature.AdvancementHolder;

public final class Advancements extends AdvancementHolder<EndermitePowder> {
    public Advancements(EndermitePowder feature) {
        super(feature);
    }

    public void usedEndermitePowder(Player player) {
        trigger("used_endermite_powder", player);
    }
}
