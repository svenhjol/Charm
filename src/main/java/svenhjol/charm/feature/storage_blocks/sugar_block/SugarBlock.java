package svenhjol.charm.feature.storage_blocks.sugar_block;

import svenhjol.charm.feature.storage_blocks.StorageBlocks;
import svenhjol.charm.feature.storage_blocks.sugar_block.common.Advancements;
import svenhjol.charm.feature.storage_blocks.sugar_block.common.Handlers;
import svenhjol.charm.feature.storage_blocks.sugar_block.common.Registers;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;
import svenhjol.charm.charmony.feature.ChildFeature;

@Feature(description = """
    Combine sugar to make a sugar block.
    Sugar blocks are affected by gravity and dissolve in water.""")
public final class SugarBlock extends CommonFeature implements ChildFeature<StorageBlocks> {
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
