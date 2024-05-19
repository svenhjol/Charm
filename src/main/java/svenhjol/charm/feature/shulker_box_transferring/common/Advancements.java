package svenhjol.charm.feature.shulker_box_transferring.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.shulker_box_transferring.ShulkerBoxTransferring;
import svenhjol.charm.foundation.feature.AdvancementHolder;

public final class Advancements extends AdvancementHolder<ShulkerBoxTransferring> {
    public Advancements(ShulkerBoxTransferring feature) {
        super(feature);
    }

    public void transferredToShulkerBox(Player player) {
        trigger("transferred_to_shulker_box", player);
    }
}
