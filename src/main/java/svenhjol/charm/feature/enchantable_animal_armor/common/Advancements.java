package svenhjol.charm.feature.enchantable_animal_armor.common;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import svenhjol.charm.feature.enchantable_animal_armor.EnchantableAnimalArmor;
import svenhjol.charm.foundation.feature.AdvancementHolder;
import svenhjol.charm.foundation.helper.PlayerHelper;

public final class Advancements extends AdvancementHolder<EnchantableAnimalArmor> {
    public Advancements(EnchantableAnimalArmor feature) {
        super(feature);
    }

    public void addEnchantmentToArmor(ServerLevel level, BlockPos pos) {
        var players = PlayerHelper.getPlayersInRange(level, pos, 4.0d);
        players.forEach(
            player -> trigger("added_enchantment_to_animal_armor", player));
    }
}
