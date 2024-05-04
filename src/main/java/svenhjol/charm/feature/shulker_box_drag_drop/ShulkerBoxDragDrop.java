package svenhjol.charm.feature.shulker_box_drag_drop;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.advancements.Advancements;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Registration;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.Optional;

public class ShulkerBoxDragDrop extends CommonFeature {

    @Override
    public String description() {
        return "Drag and drop items into a shulkerbox from within your inventory.";
    }

    @Override
    public Optional<Registration<? extends Feature>> registration() {
        return Optional.of(new CommonRegistration(this));
    }

    public static void triggerDraggedItemToShulkerBox(Player player) {
        Advancements.trigger(Charm.id("dragged_item_to_shulker_box"), player);
    }
}
