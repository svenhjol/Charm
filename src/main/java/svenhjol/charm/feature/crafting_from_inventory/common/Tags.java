package svenhjol.charm.feature.crafting_from_inventory.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import svenhjol.charm.Charm;

public final class Tags {
    public static final TagKey<Item> CRAFTING_TABLES = TagKey.create(Registries.ITEM,
        Charm.id("crafting_tables"));
}
