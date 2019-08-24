package svenhjol.meson.handler;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.entity.merchant.villager.VillagerTrades.ITrade;
import net.minecraftforge.registries.ForgeRegistries;
import svenhjol.meson.MesonLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Uses code adapted from Placebo by Shadows-of-Fire.
 * @link {https://github.com/Shadows-of-Fire/Placebo/blob/1.14/src/main/java/shadows/placebo/trading/VillagerTradingManager.java}
 */
public class VillagerTradingManager
{
    public static void setupTrades()
    {
        for (VillagerProfession prof : ForgeRegistries.PROFESSIONS) {
            Int2ObjectMap<ITrade[]> trades = VillagerTrades.field_221239_a.computeIfAbsent(prof, a -> new Int2ObjectOpenHashMap<>());
            Int2ObjectMap<List<ITrade>> mutableTrades = new Int2ObjectOpenHashMap<>();
            trades.int2ObjectEntrySet().forEach(e -> mutableTrades.put(e.getIntKey(), Lists.newArrayList(e.getValue())));
            for (int i = 1; i < 6; i++) {
                mutableTrades.computeIfAbsent(i, e -> new ArrayList<>());
            }

            /* @todo way to fire this properly, maybe as mod lifecycle hook */
            MesonLoader.allModules(module -> module.registerTrades(mutableTrades, prof));
            mutableTrades.int2ObjectEntrySet().forEach(e -> trades.put(e.getIntKey(), e.getValue().toArray(new ITrade[0])));
        }
    }
}
