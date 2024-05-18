package svenhjol.charm.feature.nether_portal_blocks.common;

import net.minecraft.world.level.portal.PortalShape;
import svenhjol.charm.feature.nether_portal_blocks.NetherPortalBlocks;
import svenhjol.charm.foundation.feature.RegisterHolder;

public final class Registers extends RegisterHolder<NetherPortalBlocks> {
    public Registers(NetherPortalBlocks feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        PortalShape.FRAME = (blockState, blockView, blockPos) -> blockState.is(Tags.NETHER_PORTAL_FRAMES);
    }
}
