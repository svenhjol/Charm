package svenhjol.charm.feature.shulker_box_drag_drop.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.shulker_box_drag_drop.ShulkerBoxDragDrop;
import svenhjol.charm.foundation.feature.AdvancementHolder;

public final class Advancements extends AdvancementHolder<ShulkerBoxDragDrop> {
    public Advancements(ShulkerBoxDragDrop feature) {
        super(feature);
    }

    public void draggedItemToShulkerBox(Player player) {
        trigger("dragged_item_to_shulker_box", player);
    }
}
