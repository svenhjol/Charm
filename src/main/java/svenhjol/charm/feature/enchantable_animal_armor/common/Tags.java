package svenhjol.charm.feature.enchantable_animal_armor.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;
import svenhjol.charm.Charm;

public final class Tags {
    public static final TagKey<Enchantment> ON_HORSE_ARMOR = TagKey.create(Registries.ENCHANTMENT,
        Charm.id("on_horse_armor"));
    public static final TagKey<Enchantment> ON_WOLF_ARMOR = TagKey.create(Registries.ENCHANTMENT,
        Charm.id("on_wolf_armor"));
}
