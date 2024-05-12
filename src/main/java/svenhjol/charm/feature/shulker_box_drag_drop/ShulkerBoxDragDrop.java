package svenhjol.charm.feature.shulker_box_drag_drop;

import svenhjol.charm.feature.shulker_box_drag_drop.common.Advancements;
import svenhjol.charm.feature.shulker_box_drag_drop.common.Handlers;
import svenhjol.charm.feature.shulker_box_drag_drop.common.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = "Drag and drop items into a shulkerbox from within your inventory.")
public final class ShulkerBoxDragDrop extends CommonFeature {
    public final Registers registers;
    public final Handlers handlers;
    public final Advancements advancements;

    public ShulkerBoxDragDrop(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
        advancements = new Advancements(this);
    }
}
