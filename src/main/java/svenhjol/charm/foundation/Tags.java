package svenhjol.charm.foundation;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import svenhjol.charm.Charm;

@Deprecated
@SuppressWarnings("unused")
public final class Tags {
    public static final TagKey<Block> NEARBY_WORKSTATIONS = TagKey.create(Registries.BLOCK,
        Charm.id("nearby_workstations"));
}
