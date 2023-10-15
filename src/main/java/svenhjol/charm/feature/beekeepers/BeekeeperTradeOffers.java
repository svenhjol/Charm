package svenhjol.charm.feature.beekeepers;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import svenhjol.charmony.helper.GenericTradeOffers;
import svenhjol.charmony.helper.TagHelper;
import svenhjol.charmony.helper.TextHelper;

import javax.annotation.Nullable;
public class BeekeeperTradeOffers {
    public static class EmeraldsForFlowers implements VillagerTrades.ItemListing {
        private final int villagerXp;
        private final int baseCost;
        private final int extraCost;
        private final int baseEmeralds;
        private final int extraEmeralds;
        private final int maxUses;

        public EmeraldsForFlowers(int baseCost, int extraCost, int baseEmeralds, int extraEmeralds, int villagerXp, int maxUses) {
            this.baseCost = baseCost;
            this.extraCost = extraCost;
            this.baseEmeralds = baseEmeralds;
            this.extraEmeralds = extraEmeralds;
            this.villagerXp = villagerXp;
            this.maxUses = maxUses;
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(Entity merchant, RandomSource random) {
            var registryAccess = merchant.level().registryAccess();
            var tag = Beekeepers.BEEKEEPER_SELLS_FLOWERS;
            var flowers = TagHelper.getValues(registryAccess.registryOrThrow(tag.registry()), tag);
            var index = random.nextInt(flowers.size());
            var flower = flowers.get(index);
            var cost = baseCost + random.nextInt(extraCost + 1);
            var emeralds = baseEmeralds + random.nextInt(extraEmeralds + 1);

            return new MerchantOffer(
                new ItemStack(flower, cost),
                new ItemStack(Items.EMERALD, emeralds),
                maxUses,
                villagerXp,
                0.2F
            );
        }
    }

    public static class PopulatedBeehiveForEmeralds implements VillagerTrades.ItemListing {
        static final int NUMBER_OF_BEES = 2;
        private final int villagerXp;
        private final int baseEmeralds;
        private final int extraEmeralds;
        private final int maxUses;

        public PopulatedBeehiveForEmeralds(int baseEmeralds, int extraEmeralds, int villagerXp, int maxUses) {
            this.baseEmeralds = baseEmeralds;
            this.extraEmeralds = extraEmeralds;
            this.villagerXp = villagerXp;
            this.maxUses = maxUses;
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(Entity merchant, RandomSource random) {
            var out = new ItemStack(Items.BEEHIVE);

            var beehive = new BeehiveBlockEntity(BlockPos.ZERO, Blocks.BEEHIVE.defaultBlockState());
            var beesTag = new CompoundTag();
            var honeyTag = new CompoundTag();

            for (int i = 0; i < NUMBER_OF_BEES; i++) {
                var bee = new Bee(EntityType.BEE, merchant.level());
                beehive.addOccupantWithPresetTicks(bee, false, 0);
            }
            beesTag.put("Bees", beehive.writeBees());
            honeyTag.putInt("honey_level", 0);
            out.addTagElement("BlockEntityTag", beesTag);
            out.addTagElement("BlockStateTag", honeyTag);
            out.setHoverName(TextHelper.translatable("item.charm.populated_beehive"));

            return new MerchantOffer(
                GenericTradeOffers.getStack(random, Items.EMERALD, baseEmeralds, extraEmeralds),
                out,
                maxUses,
                villagerXp,
                0.2F
            );
        }
    }
}
