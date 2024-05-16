package svenhjol.charm.feature.storage_blocks;

import svenhjol.charm.feature.storage_blocks.ender_pearl_block.EnderPearlBlockClient;
import svenhjol.charm.feature.storage_blocks.gunpowder_block.GunpowderBlockClient;
import svenhjol.charm.feature.storage_blocks.sugar_block.SugarBlockClient;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;
import svenhjol.charm.foundation.common.CommonResolver;
import svenhjol.charm.foundation.feature.ChildFeature;

import java.util.List;

@Feature
public final class StorageBlocksClient extends ClientFeature implements CommonResolver<StorageBlocks> {
    public StorageBlocksClient(ClientLoader loader) {
        super(loader);
    }

    @Override
    public List<ChildFeature<StorageBlocksClient>> children() {
        return List.of(
            new EnderPearlBlockClient(loader()),
            new GunpowderBlockClient(loader()),
            new SugarBlockClient(loader())
        );
    }

    @Override
    public Class<StorageBlocks> typeForCommon() {
        return StorageBlocks.class;
    }
}
