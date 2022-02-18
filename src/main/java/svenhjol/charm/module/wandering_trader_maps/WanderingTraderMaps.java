package svenhjol.charm.module.wandering_trader_maps;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ConfiguredStructureTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.helper.MapHelper;
import svenhjol.charm.helper.VillagerHelper;
import svenhjol.charm.loader.CharmModule;

import java.util.Random;

@CommonModule(mod = Charm.MOD_ID, description = "Wandering traders have a chance to sell maps to other villages.")
public class WanderingTraderMaps extends CharmModule {
    @Override
    public void runWhenEnabled() {
        for (int i = 0; i < 3; i++) {
            VillagerHelper.addWanderingTrade(new VillageMapForEmeralds(), false);
        }
    }

    public static class VillageMapForEmeralds implements VillagerTrades.ItemListing {
        @Override
        public MerchantOffer getOffer(Entity trader, Random random) {
            if (!trader.level.isClientSide) {
                var villageMap = new VillageMap();
                ItemStack stack = villageMap.getMap((ServerLevel) trader.level, trader.blockPosition());
                if (stack != null) {
                    ItemStack in1 = new ItemStack(Items.EMERALD, villageMap.getCost(random));
                    ItemStack in2 = new ItemStack(Items.COMPASS);
                    return new MerchantOffer(in1, in2, stack, 1, 5, 0.2F);
                }
            }

            return null;
        }
    }

    public static class VillageMap implements TraderMap {
        public TagKey<ConfiguredStructureFeature<?, ?>> structureTag;

        public VillageMap() {
            this.structureTag = ConfiguredStructureTags.VILLAGE;
        }

        @Override
        public ItemStack getMap(ServerLevel level, BlockPos pos) {
            int color = 0x662200;
            BlockPos nearestStructure = level.findNearestMapFeature(structureTag, pos, 2000, true);
            if (nearestStructure == null) return null;

            TranslatableComponent structureName = new TranslatableComponent("structure.charm.village");
            TranslatableComponent mapName = new TranslatableComponent("filled_map.charm.trader_map", structureName);
            return MapHelper.create(level, nearestStructure, mapName, MapDecoration.Type.TARGET_X, color);
        }

        @Override
        public int getCost(Random rand) {
            return rand.nextInt(2) + 2;
        }
    }

    public interface TraderMap {
        ItemStack getMap(ServerLevel level, BlockPos pos);

        int getCost(Random rand);
    }
}
