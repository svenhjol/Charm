package svenhjol.charm.feature.storage_blocks.sugar_block;

import svenhjol.charm.feature.storage_blocks.StorageBlocksClient;
import svenhjol.charm.feature.storage_blocks.sugar_block.client.Registers;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.charmony.common.CommonResolver;
import svenhjol.charm.charmony.feature.ChildFeature;

@Feature
public final class SugarBlockClient extends ClientFeature implements ChildFeature<StorageBlocksClient>, CommonResolver<SugarBlock> {
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
    public Class<SugarBlock> typeForCommon() {
        return SugarBlock.class;
    }
}
