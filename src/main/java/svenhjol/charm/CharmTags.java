package svenhjol.charm;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class CharmTags {
    public static final TagKey<Item> CHESTS = TagKey.create(BuiltInRegistries.ITEM.key(),
        Charm.instance().makeId("chests/normal"));

    public static final TagKey<Item> COLORED_DYES = TagKey.create(BuiltInRegistries.ITEM.key(),
        Charm.instance().makeId("colored_dyes"));

    public static final TagKey<Block> PISTONS = TagKey.create(BuiltInRegistries.BLOCK.key(),
        Charm.instance().makeId("pistons"));

    public static final TagKey<Block> STICKY_PISTONS = TagKey.create(BuiltInRegistries.BLOCK.key(),
        Charm.instance().makeId("sticky_pistons"));

    public static final TagKey<Block> PISTON_HEADS = TagKey.create(BuiltInRegistries.BLOCK.key(),
        Charm.instance().makeId("piston_heads"));

    public static final TagKey<Block> MOVING_PISTONS = TagKey.create(BuiltInRegistries.BLOCK.key(),
        Charm.instance().makeId("moving_pistons"));
}
