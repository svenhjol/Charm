package svenhjol.charm.feature.storage_blocks.gunpowder_block;

import svenhjol.charm.feature.storage_blocks.StorageBlocks;
import svenhjol.charm.feature.storage_blocks.gunpowder_block.common.Advancements;
import svenhjol.charm.feature.storage_blocks.gunpowder_block.common.Providers;
import svenhjol.charm.feature.storage_blocks.gunpowder_block.common.Registers;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;
import svenhjol.charm.foundation.feature.ChildFeature;

@Feature(description = """
    Combine gunpowder to make a gunpowder block.
    Gunpowder blocks are affected by gravity and dissolve in lava.""")
public final class GunpowderBlock extends CommonFeature implements ChildFeature<StorageBlocks> {
    public final Registers registers;
    public final Advancements advancements;
    public final Providers providers;

    @Configurable(name = "TNT from gunpowder block and sand", description = "If true, adds a recipe for TNT using a gunpowder block and any sand.")
    private static boolean tntRecipe = true;

    public GunpowderBlock(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        advancements = new Advancements(this);
        providers = new Providers(this);
    }

    public boolean tntRecipe() {
        return tntRecipe;
    }

    @Override
    public Class<StorageBlocks> typeForParent() {
        return StorageBlocks.class;
    }
}
