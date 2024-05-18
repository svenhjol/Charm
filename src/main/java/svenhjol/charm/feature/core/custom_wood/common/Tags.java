package svenhjol.charm.feature.core.custom_wood.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import svenhjol.charm.Charm;

public final class Tags {
    public static final TagKey<Block> BARRELS = TagKey.create(Registries.BLOCK,
        Charm.id("barrels"));
    public static final TagKey<Block> CHISELED_BOOKSHELVES = TagKey.create(Registries.BLOCK,
        Charm.id("chiseled_bookshelves"));
    public static final TagKey<Block> LADDERS = TagKey.create(Registries.BLOCK,
        Charm.id("ladders"));
    public static final TagKey<Block> OVERWORLD_STRIPPED_LOGS = TagKey.create(Registries.BLOCK,
        Charm.id("overworld_stripped_logs"));
}
