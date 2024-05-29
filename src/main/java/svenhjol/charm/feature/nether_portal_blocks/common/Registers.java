package svenhjol.charm.feature.nether_portal_blocks.common;

import net.minecraft.world.level.portal.PortalShape;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.nether_portal_blocks.NetherPortalBlocks;

public final class Registers extends RegisterHolder<NetherPortalBlocks> {
    public Registers(NetherPortalBlocks feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        PortalShape.FRAME = (blockState, blockView, blockPos) -> blockState.is(Tags.NETHER_PORTAL_FRAMES);
    }
}
