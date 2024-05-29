package svenhjol.charm.feature.nether_portal_blocks;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;
import svenhjol.charm.feature.nether_portal_blocks.common.Registers;

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
