package svenhjol.charm.feature.grindstone_disenchanting.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.grindstone_disenchanting.GrindstoneDisenchanting;
import svenhjol.charm.feature.core.custom_advancements.common.AdvancementHolder;

public final class Advancements extends AdvancementHolder<GrindstoneDisenchanting> {
    public Advancements(GrindstoneDisenchanting feature) {
        super(feature);
    }

    public void extractedEnchantment(Player player) {
        trigger("extracted_enchantment", player);
    }
}
