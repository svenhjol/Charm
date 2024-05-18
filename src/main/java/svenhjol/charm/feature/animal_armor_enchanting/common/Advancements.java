package svenhjol.charm.feature.animal_armor_enchanting.common;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import svenhjol.charm.feature.animal_armor_enchanting.AnimalArmorEnchanting;
import svenhjol.charm.foundation.feature.AdvancementHolder;
import svenhjol.charm.foundation.helper.PlayerHelper;

public final class Advancements extends AdvancementHolder<AnimalArmorEnchanting> {
    public Advancements(AnimalArmorEnchanting feature) {
        super(feature);
    }

    public void addedEnchantmentToArmor(ServerLevel level, BlockPos pos) {
        var players = PlayerHelper.getPlayersInRange(level, pos, 4.0d);
        players.forEach(
            player -> trigger("added_enchantment_to_animal_armor", player));
    }
}
