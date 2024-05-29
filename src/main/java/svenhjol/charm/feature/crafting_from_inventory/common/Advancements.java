package svenhjol.charm.feature.crafting_from_inventory.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.crafting_from_inventory.CraftingFromInventory;
import svenhjol.charm.feature.core.custom_advancements.common.AdvancementHolder;

public final class Advancements extends AdvancementHolder<CraftingFromInventory> {
    public Advancements(CraftingFromInventory feature) {
        super(feature);
    }

    public void usedCraftingTable(Player player) {
        trigger("used_portable_crafting_table", player);
    }
}
