package svenhjol.charm.feature.endermite_powder.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import svenhjol.charm.Charm;

public final class Tags {
    public static final TagKey<Structure> ENDERMITE_POWDER_LOCATED = TagKey.create(Registries.STRUCTURE,
        Charm.id("endermite_powder_located"));
}
