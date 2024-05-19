package svenhjol.charm.feature.shulker_box_transferring;

import svenhjol.charm.feature.shulker_box_transferring.common.Advancements;
import svenhjol.charm.feature.shulker_box_transferring.common.Handlers;
import svenhjol.charm.feature.shulker_box_transferring.common.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = "Drag and drop items into a shulkerbox from within your inventory.")
public final class ShulkerBoxTransferring extends CommonFeature {
    public final Registers registers;
    public final Handlers handlers;
    public final Advancements advancements;

    public ShulkerBoxTransferring(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
        advancements = new Advancements(this);
    }
}
