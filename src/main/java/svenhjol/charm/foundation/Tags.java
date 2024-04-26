package svenhjol.charm.foundation;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import svenhjol.charm.Charm;

@SuppressWarnings("unused")
public class Tags {
    public static final TagKey<Block> BARRELS = TagKey.create(Registries.BLOCK,
        new ResourceLocation(Charm.ID, "barrels"));

    public static final TagKey<Block> CHISELED_BOOKSHELVES = TagKey.create(Registries.BLOCK,
        new ResourceLocation(Charm.ID, "chiseled_bookshelves"));

    public static final TagKey<Block> LADDERS = TagKey.create(Registries.BLOCK,
        new ResourceLocation(Charm.ID, "ladders"));

    public static final TagKey<Block> MOVING_PISTONS = TagKey.create(Registries.BLOCK,
            new ResourceLocation(Charm.ID, "moving_pistons"));

    public static final TagKey<Block> PISTONS = TagKey.create(Registries.BLOCK,
            new ResourceLocation(Charm.ID, "pistons"));

    public static final TagKey<Block> PISTON_HEADS = TagKey.create(Registries.BLOCK,
            new ResourceLocation(Charm.ID, "piston_heads"));

    public static final TagKey<Block> STICKY_PISTONS = TagKey.create(Registries.BLOCK,
            new ResourceLocation(Charm.ID, "sticky_pistons"));
}
