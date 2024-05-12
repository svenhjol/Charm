package svenhjol.charm.feature.shulker_box_drag_drop.common;

import svenhjol.charm.api.event.ItemDragDropEvent;
import svenhjol.charm.api.event.LevelLoadEvent;
import svenhjol.charm.feature.shulker_box_drag_drop.ShulkerBoxDragDrop;
import svenhjol.charm.foundation.feature.RegisterHolder;

public final class Registers extends RegisterHolder<ShulkerBoxDragDrop> {
    public Registers(ShulkerBoxDragDrop feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        ItemDragDropEvent.INSTANCE.handle(feature().handlers::itemDragDrop);
        LevelLoadEvent.INSTANCE.handle(feature().handlers::levelLoad);
    }
}
