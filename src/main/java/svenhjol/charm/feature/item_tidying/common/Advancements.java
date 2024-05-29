package svenhjol.charm.feature.item_tidying.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.item_tidying.ItemTidying;
import svenhjol.charm.feature.core.custom_advancements.common.AdvancementHolder;

public final class Advancements extends AdvancementHolder<ItemTidying> {
    public Advancements(ItemTidying feature) {
        super(feature);
    }

    public void tidiedItems(Player player) {
        trigger("tidied_items", player);
    }
}
