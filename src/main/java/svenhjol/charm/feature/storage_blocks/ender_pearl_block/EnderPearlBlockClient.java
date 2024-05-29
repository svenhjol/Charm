package svenhjol.charm.feature.storage_blocks.ender_pearl_block;

import svenhjol.charm.feature.storage_blocks.StorageBlocksClient;
import svenhjol.charm.feature.storage_blocks.ender_pearl_block.client.Registers;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.charmony.common.CommonResolver;
import svenhjol.charm.charmony.feature.ChildFeature;

@Feature
public final class EnderPearlBlockClient extends ClientFeature
    implements ChildFeature<StorageBlocksClient>, CommonResolver<EnderPearlBlock> {
    public final Registers registers;

    public EnderPearlBlockClient(ClientLoader loader) {
        super(loader);
        registers = new Registers(this);
    }

    @Override
    public Class<StorageBlocksClient> typeForParent() {
        return StorageBlocksClient.class;
    }

    @Override
    public Class<EnderPearlBlock> typeForCommon() {
        return EnderPearlBlock.class;
    }
}
