package svenhjol.charm.feature.glint_coloring.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import svenhjol.charm.Charm;

public final class Tags {
    public static final TagKey<Item> COLORED_DYES = TagKey.create(Registries.ITEM,
        Charm.id("colored_dyes"));
}
