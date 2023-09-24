package svenhjol.charm;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class CharmTags {
    public static final TagKey<Item> CHESTS = TagKey.create(BuiltInRegistries.ITEM.key(),
        Charm.instance().makeId("chests/normal"));
    public static final TagKey<Item> COLORED_DYES = TagKey.create(BuiltInRegistries.ITEM.key(),
        Charm.instance().makeId("colored_dyes"));
    public static final TagKey<Item> HAS_SUSPICIOUS_EFFECTS = TagKey.create(BuiltInRegistries.ITEM.key(),
        Charm.instance().makeId("has_suspicious_effects"));
}
