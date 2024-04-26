package svenhjol.charm.foundation;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import svenhjol.charm.Charm;

@SuppressWarnings("unused")
public class Tags {
    public static final TagKey<Block> BARRELS = TagKey.create(Registries.BLOCK,
        Charm.id("barrels"));

    public static final TagKey<Block> CHISELED_BOOKSHELVES = TagKey.create(Registries.BLOCK,
        Charm.id("chiseled_bookshelves"));

    public static final TagKey<Block> LADDERS = TagKey.create(Registries.BLOCK,
        Charm.id("ladders"));

    public static final TagKey<Block> MOVING_PISTONS = TagKey.create(Registries.BLOCK,
        Charm.id("moving_pistons"));

    public static final TagKey<Block> PISTONS = TagKey.create(Registries.BLOCK,
        Charm.id("pistons"));

    public static final TagKey<Block> PISTON_HEADS = TagKey.create(Registries.BLOCK,
        Charm.id("piston_heads"));

    public static final TagKey<Block> STICKY_PISTONS = TagKey.create(Registries.BLOCK,
        Charm.id("sticky_pistons"));
}
