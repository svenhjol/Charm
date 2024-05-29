package svenhjol.charm.feature.storage_blocks.gunpowder_block;

import svenhjol.charm.feature.storage_blocks.StorageBlocksClient;
import svenhjol.charm.feature.storage_blocks.gunpowder_block.client.Registers;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.charmony.common.CommonResolver;
import svenhjol.charm.charmony.feature.ChildFeature;

@Feature
public final class GunpowderBlockClient extends ClientFeature implements CommonResolver<GunpowderBlock>, ChildFeature<StorageBlocksClient> {
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
