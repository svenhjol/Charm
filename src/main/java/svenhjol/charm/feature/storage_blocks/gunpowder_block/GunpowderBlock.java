package svenhjol.charm.feature.storage_blocks.gunpowder_block;

import svenhjol.charm.feature.storage_blocks.StorageBlocks;
import svenhjol.charm.feature.storage_blocks.gunpowder_block.common.Advancements;
import svenhjol.charm.feature.storage_blocks.gunpowder_block.common.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;
import svenhjol.charm.foundation.feature.SubFeature;

@Feature(description = """
    Combine gunpowder to make a gunpowder block.
    Gunpowder blocks are affected by gravity and dissolve in lava.""")
public final class GunpowderBlock extends CommonFeature implements SubFeature<StorageBlocks> {
    public final Registers registers;
    public final Advancements advancements;

    public GunpowderBlock(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        advancements = new Advancements(this);
    }

    @Override
    public Class<StorageBlocks> typeForParent() {
        return StorageBlocks.class;
    }
}
