package svenhjol.charm.feature.storage_blocks.sugar_block;

import svenhjol.charm.feature.storage_blocks.StorageBlocks;
import svenhjol.charm.feature.storage_blocks.sugar_block.common.Advancements;
import svenhjol.charm.feature.storage_blocks.sugar_block.common.Handlers;
import svenhjol.charm.feature.storage_blocks.sugar_block.common.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;
import svenhjol.charm.foundation.feature.SubFeature;

@Feature(description = """
    Combine sugar to make a sugar block.
    Sugar blocks are affected by gravity and dissolve in water.""")
public final class SugarBlock extends CommonFeature implements SubFeature<StorageBlocks> {
    public final Registers registers;
    public final Handlers handlers;
    public final Advancements advancements;

    public SugarBlock(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
        advancements = new Advancements(this);
    }

    @Override
    public Class<StorageBlocks> typeForParent() {
        return StorageBlocks.class;
    }
}
