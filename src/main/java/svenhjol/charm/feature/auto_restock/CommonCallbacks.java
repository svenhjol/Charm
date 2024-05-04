package svenhjol.charm.feature.auto_restock;

import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class CommonCallbacks {
    public static void addItemUsedStat(Player player, ItemStack stack) {
        player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
    }
}
