package svenhjol.charm.feature.mooblooms.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.core.custom_advancements.common.AdvancementHolder;
import svenhjol.charm.feature.mooblooms.Mooblooms;

public final class Advancements extends AdvancementHolder<Mooblooms> {
    public Advancements(Mooblooms feature) {
        super(feature);
    }

    public void milkedMoobloom(Player player) {
        trigger("milked_moobloom", player);
    }

    public void milkedRareMoobloom(Player player) {
        trigger("milked_rare_moobloom", player);
    }
}
