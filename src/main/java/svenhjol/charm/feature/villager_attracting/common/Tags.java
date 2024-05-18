package svenhjol.charm.feature.villager_attracting.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import svenhjol.charm.Charm;

public final class Tags {
    public static final TagKey<Item> VILLAGER_LOVED = TagKey.create(Registries.ITEM,
        Charm.id("villager_loved"));
}
