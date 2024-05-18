package svenhjol.charm.feature.extra_portal_frames;

import svenhjol.charm.feature.extra_portal_frames.common.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = """
    Adds more blocks that can be used to build nether portal frames. By default this adds Crying Obsidian.
    The item tag 'nether_portal_frames' can be used to configure the blocks that can be used to build portal frames.""")
public class ExtraPortalFrames extends CommonFeature {
    public final Registers registers;

    public ExtraPortalFrames(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
    }
}
