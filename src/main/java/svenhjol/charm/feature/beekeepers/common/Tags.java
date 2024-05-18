package svenhjol.charm.feature.beekeepers.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import svenhjol.charm.Charm;

public final class Tags {
    public static final TagKey<Item> BEEKEEPER_SELLS_FLOWERS = TagKey.create(Registries.ITEM,
        Charm.id("beekeeper_sells_flowers"));
}
