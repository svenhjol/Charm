package svenhjol.charm.feature.extractable_enchantments.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.extractable_enchantments.ExtractableEnchantments;
import svenhjol.charm.foundation.feature.AdvancementHolder;

public final class Advancements extends AdvancementHolder<ExtractableEnchantments> {
    public Advancements(ExtractableEnchantments feature) {
        super(feature);
    }

    public void extractedEnchantment(Player player) {
        trigger("extracted_enchantment", player);
    }
}
