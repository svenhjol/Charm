package svenhjol.charm.feature.aerial_affinity.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.aerial_affinity.AerialAffinity;
import svenhjol.charm.foundation.feature.AdvancementHolder;

public final class Advancements extends AdvancementHolder<AerialAffinity> {
    public Advancements(AerialAffinity feature) {
        super(feature);
    }

    public void usedAerialAffinity(Player player) {
        trigger("used_aerial_affinity", player);
    }
}
