package svenhjol.charm.feature.storage_blocks;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.charmony.feature.ChildFeature;
import svenhjol.charm.charmony.feature.LinkedFeature;
import svenhjol.charm.feature.storage_blocks.ender_pearl_block.EnderPearlBlockClient;
import svenhjol.charm.feature.storage_blocks.gunpowder_block.GunpowderBlockClient;
import svenhjol.charm.feature.storage_blocks.sugar_block.SugarBlockClient;

import java.util.List;

@Feature
public final class StorageBlocksClient extends ClientFeature implements LinkedFeature<StorageBlocks> {
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
    public Class<StorageBlocks> typeForLinked() {
        return StorageBlocks.class;
    }
}
