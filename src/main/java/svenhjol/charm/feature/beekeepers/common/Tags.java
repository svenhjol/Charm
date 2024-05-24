package svenhjol.charm.feature.beekeepers.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import svenhjol.charm.Charm;

public final class Tags {
    public static final TagKey<Item> BEEKEEPERS_SELL_FLOWERS = TagKey.create(Registries.ITEM,
        Charm.id("beekeepers_sell_flowers"));
}
