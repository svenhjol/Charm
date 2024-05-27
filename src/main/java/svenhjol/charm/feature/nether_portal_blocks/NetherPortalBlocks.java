package svenhjol.charm.feature.nether_portal_blocks;

import svenhjol.charm.feature.nether_portal_blocks.common.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = """
    Adds more blocks that can be used to build nether portals. By default this adds Crying Obsidian.
    The item tag 'nether_portal_blocks' can be used to configure the blocks that can be used to build portals.""")
public final class NetherPortalBlocks extends CommonFeature {
    public final Registers registers;

    public NetherPortalBlocks(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
    }
}
