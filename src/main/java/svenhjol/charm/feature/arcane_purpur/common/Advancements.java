package svenhjol.charm.feature.arcane_purpur.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.arcane_purpur.ArcanePurpur;
import svenhjol.charm.foundation.feature.AdvancementHolder;

public final class Advancements extends AdvancementHolder<ArcanePurpur> {
    public Advancements(ArcanePurpur feature) {
        super(feature);
    }

    public void teleportedToBlock(Player player) {
        trigger("teleported_to_block", player);
    }
}
