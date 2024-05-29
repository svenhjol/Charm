package svenhjol.charm.feature.item_frame_hiding;

import svenhjol.charm.feature.item_frame_hiding.common.Advancements;
import svenhjol.charm.feature.item_frame_hiding.common.Handlers;
import svenhjol.charm.feature.item_frame_hiding.common.Networking;
import svenhjol.charm.feature.item_frame_hiding.common.Registers;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;

@Feature(description = "Add amethyst shards to item frames to hide them.")
public final class ItemFrameHiding extends CommonFeature {
    public final Registers registers;
    public final Handlers handlers;
    public final Networking networking;
    public final Advancements advancements;

    public ItemFrameHiding(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
        networking = new Networking(this);
        advancements = new Advancements(this);
    }
}
