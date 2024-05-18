package svenhjol.charm.feature.extra_portal_frames.common;

import net.minecraft.world.level.portal.PortalShape;
import svenhjol.charm.feature.extra_portal_frames.ExtraPortalFrames;
import svenhjol.charm.foundation.feature.RegisterHolder;

public final class Registers extends RegisterHolder<ExtraPortalFrames> {
    public Registers(ExtraPortalFrames feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        PortalShape.FRAME = (blockState, blockView, blockPos) -> blockState.is(Tags.NETHER_PORTAL_FRAMES);
    }
}
