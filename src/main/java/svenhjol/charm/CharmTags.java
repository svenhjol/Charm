package svenhjol.charm;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class CharmTags {
    public static final TagKey<Item> CHESTS = TagKey.create(BuiltInRegistries.ITEM.key(),
        Charm.instance().makeId("chests/normal"));
}
