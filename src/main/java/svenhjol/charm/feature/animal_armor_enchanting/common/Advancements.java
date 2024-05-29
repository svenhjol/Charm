package svenhjol.charm.feature.animal_armor_enchanting.common;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import svenhjol.charm.charmony.helper.PlayerHelper;
import svenhjol.charm.feature.animal_armor_enchanting.AnimalArmorEnchanting;
import svenhjol.charm.feature.core.custom_advancements.common.AdvancementHolder;

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
