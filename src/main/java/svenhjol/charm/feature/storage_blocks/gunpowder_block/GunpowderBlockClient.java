package svenhjol.charm.feature.storage_blocks.gunpowder_block;

import svenhjol.charm.feature.storage_blocks.StorageBlocksClient;
import svenhjol.charm.feature.storage_blocks.gunpowder_block.client.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;
import svenhjol.charm.foundation.common.CommonResolver;
import svenhjol.charm.foundation.feature.SubFeature;

@Feature
public final class GunpowderBlockClient extends ClientFeature implements CommonResolver<GunpowderBlock>, SubFeature<StorageBlocksClient> {
    public final Registers registers;

    public GunpowderBlockClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
    }

    @Override
    public Class<GunpowderBlock> typeForCommon() {
        return GunpowderBlock.class;
    }

    @Override
    public Class<StorageBlocksClient> typeForParent() {
        return StorageBlocksClient.class;
    }
}
