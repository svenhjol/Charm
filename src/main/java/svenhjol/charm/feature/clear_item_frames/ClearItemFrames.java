package svenhjol.charm.feature.clear_item_frames;

import svenhjol.charm.feature.clear_item_frames.common.Advancements;
import svenhjol.charm.feature.clear_item_frames.common.Handlers;
import svenhjol.charm.feature.clear_item_frames.common.Networking;
import svenhjol.charm.feature.clear_item_frames.common.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = "Add amethyst shards to item frames to make them invisible.")
public final class ClearItemFrames extends CommonFeature {
    public final Registers registers;
    public final Handlers handlers;
    public final Networking networking;
    public final Advancements advancements;

    public ClearItemFrames(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
        networking = new Networking(this);
        advancements = new Advancements(this);
    }
}
