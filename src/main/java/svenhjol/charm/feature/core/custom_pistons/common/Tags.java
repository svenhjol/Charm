package svenhjol.charm.feature.core.custom_pistons.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import svenhjol.charm.Charm;

public final class Tags {
    public static final TagKey<Block> MOVING_PISTONS = TagKey.create(Registries.BLOCK,
        Charm.id("moving_pistons"));
    public static final TagKey<Block> PISTONS = TagKey.create(Registries.BLOCK,
        Charm.id("pistons"));
    public static final TagKey<Block> PISTON_HEADS = TagKey.create(Registries.BLOCK,
        Charm.id("piston_heads"));
    public static final TagKey<Block> STICKY_PISTONS = TagKey.create(Registries.BLOCK,
        Charm.id("sticky_pistons"));
}
