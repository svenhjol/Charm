package svenhjol.charm.module.extra_wandering_trades;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.helper.LogHelper;
import svenhjol.charm.helper.VillagerHelper;
import svenhjol.charm.loader.CharmModule;

import java.util.HashMap;
import java.util.Map;

@CommonModule(mod = Charm.MOD_ID, description = "Adds Charm items as trades for the Wandering Trader.")
public class ExtraWanderingTrades extends CharmModule {
    private static final Map<ItemLike, Pair<Integer, Integer>> ITEMS = new HashMap<>();

    private static final Map<ItemLike, Pair<Integer, Integer>> RARE_ITEMS = new HashMap<>();

    @Override
    public void runWhenEnabled() {
        for (Map.Entry<ItemLike, Pair<Integer, Integer>> entry : ITEMS.entrySet()) {
            var item = entry.getKey();
            var maxCount = entry.getValue().getFirst();
            var minCost = entry.getValue().getSecond();
            addTrade(item, maxCount, minCost, false);
        }

        for (Map.Entry<ItemLike, Pair<Integer, Integer>> entry : RARE_ITEMS.entrySet()) {
            var item = entry.getKey();
            var maxCount = entry.getValue().getFirst();
            var minCost = entry.getValue().getSecond();
            LogHelper.debug(getClass(), "rare: " + item.asItem().getDescriptionId() + ", count: " + maxCount + ", cost: " + minCost);
            addTrade(item, maxCount, minCost, true);
        }
    }

    public static void registerItem(ItemLike item, int maxCount, int minCost) {
        ITEMS.put(item, Pair.of(maxCount, minCost));
    }

    public static void registerRareItem(ItemLike item, int maxCount, int minCost) {
        RARE_ITEMS.put(item, Pair.of(maxCount, minCost));
    }

    private void addTrade(ItemLike item, int maxCount, int minCost, boolean isRare) {
        var itemKey = item.asItem().getDescriptionId().split("\\.");
        LogHelper.debug(getClass(), (isRare ? "rare" : "normal") + ": " + itemKey[itemKey.length - 1] + ", count: " + maxCount + ", cost: " + minCost);
        VillagerHelper.addWanderingTrade((entity, random) -> {
            var in = new ItemStack(Items.EMERALD, minCost);
            var out = new ItemStack(item, maxCount);
            return new MerchantOffer(in, out, 1, 1, 1);
        }, isRare);
    }
}
