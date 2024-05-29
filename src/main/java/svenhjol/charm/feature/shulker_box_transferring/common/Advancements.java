package svenhjol.charm.feature.shulker_box_transferring.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.core.custom_advancements.common.AdvancementHolder;
import svenhjol.charm.feature.shulker_box_transferring.ShulkerBoxTransferring;

public final class Advancements extends AdvancementHolder<ShulkerBoxTransferring> {
    public Advancements(ShulkerBoxTransferring feature) {
        super(feature);
    }

    public void transferredToShulkerBox(Player player) {
        trigger("transferred_to_shulker_box", player);
    }
}
