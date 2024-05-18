package svenhjol.charm.feature.item_frame_hiding.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.item_frame_hiding.ItemFrameHiding;
import svenhjol.charm.foundation.feature.AdvancementHolder;

public final class Advancements extends AdvancementHolder<ItemFrameHiding> {
    public Advancements(ItemFrameHiding feature) {
        super(feature);
    }

    public void hiddenItemFrame(Player player) {
        trigger("hidden_item_frame", player);
    }
}
