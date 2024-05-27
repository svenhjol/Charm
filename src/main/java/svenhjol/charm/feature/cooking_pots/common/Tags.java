package svenhjol.charm.feature.cooking_pots.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import svenhjol.charm.Charm;

public final class Tags {
    public static final TagKey<Block> COOKING_HEAT_SOURCES = TagKey.create(Registries.BLOCK,
        Charm.id("cooking_heat_sources"));
}
