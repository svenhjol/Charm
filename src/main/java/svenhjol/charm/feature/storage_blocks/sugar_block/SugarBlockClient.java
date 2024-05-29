package svenhjol.charm.feature.storage_blocks.sugar_block;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.charmony.feature.ChildFeature;
import svenhjol.charm.charmony.feature.LinkedFeature;
import svenhjol.charm.feature.storage_blocks.StorageBlocksClient;
import svenhjol.charm.feature.storage_blocks.sugar_block.client.Registers;

@Feature
public final class SugarBlockClient extends ClientFeature implements ChildFeature<StorageBlocksClient>, LinkedFeature<SugarBlock> {
    public final Registers registers;

    public SugarBlockClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
    }

    @Override
    public Class<StorageBlocksClient> typeForParent() {
        return StorageBlocksClient.class;
    }

    @Override
    public Class<SugarBlock> typeForLinked() {
        return SugarBlock.class;
    }
}
