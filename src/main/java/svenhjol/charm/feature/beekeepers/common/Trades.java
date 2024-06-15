package svenhjol.charm.feature.beekeepers.common;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import svenhjol.charm.charmony.common.helper.TagHelper;
import svenhjol.charm.charmony.common.villages.GenericTrades;
import svenhjol.charm.charmony.feature.FeatureHolder;
import svenhjol.charm.charmony.helper.TextHelper;
import svenhjol.charm.feature.beekeepers.Beekeepers;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Trades extends FeatureHolder<Beekeepers> {
    public Trades(Beekeepers feature) {
        super(feature);
    }

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
            var tag = Tags.BEEKEEPERS_SELL_FLOWERS;
            var flowers = TagHelper.getValues(registryAccess.registryOrThrow(tag.registry()), tag);
            var index = random.nextInt(flowers.size());
            var flower = flowers.get(index);
            var cost = baseCost + random.nextInt(extraCost + 1);
            var emeralds = baseEmeralds + random.nextInt(extraEmeralds + 1);

            return new MerchantOffer(
                GenericTrades.getCost(flower, cost),
                new ItemStack(Items.EMERALD, emeralds),
                maxUses,
                villagerXp,
                0.2F
            );
        }
    }

    public static class TallFlowerForEmeralds implements VillagerTrades.ItemListing {
        private final int villagerXp;
        private final int baseEmeralds;
        private final int extraEmeralds;
        private final int maxUses;

        public TallFlowerForEmeralds(int baseEmeralds, int extraEmeralds, int villagerXp, int maxUses) {
            this.baseEmeralds = baseEmeralds;
            this.extraEmeralds = extraEmeralds;
            this.villagerXp = villagerXp;
            this.maxUses = maxUses;
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(Entity entity, RandomSource random) {
            var flowers = List.of(
                Items.SUNFLOWER,
                Items.PEONY,
                Items.LILAC,
                Items.ROSE_BUSH
            );

            var flower = new ItemStack(flowers.get(random.nextInt(flowers.size())));

            return new MerchantOffer(
                GenericTrades.getCost(random, Items.EMERALD, baseEmeralds, extraEmeralds),
                flower,
                maxUses,
                villagerXp,
                0.2F
            );
        }
    }

    public static class EnchantedShearsForEmeralds implements VillagerTrades.ItemListing {
        private final int villagerXp;
        private final int baseEmeralds;
        private final int extraEmeralds;
        private final int maxUses;

        public EnchantedShearsForEmeralds(int baseEmeralds, int extraEmeralds, int villagerXp, int maxUses) {
            this.baseEmeralds = baseEmeralds;
            this.extraEmeralds = extraEmeralds;
            this.villagerXp = villagerXp;
            this.maxUses = maxUses;
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(Entity merchant, RandomSource random) {
            Map<Enchantment, Integer> enchantments = new HashMap<>();
            var shears = new ItemStack(Items.SHEARS);
            var cost = baseEmeralds;

            int enchantmentLevel;
            if (random.nextDouble() < 0.1d) {
                enchantmentLevel = 3;
            } else {
                enchantmentLevel = random.nextInt(2) + 1;
            }

            enchantments.put(Enchantments.UNBREAKING, enchantmentLevel);
            EnchantmentHelper.setEnchantments(enchantments, shears);

            if (enchantmentLevel > 1) {
                cost += (enchantmentLevel * enchantmentLevel);
            }

            return new MerchantOffer(
                GenericTrades.getCost(random, Items.EMERALD, cost, extraEmeralds),
                shears,
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
                GenericTrades.getCost(random, Items.EMERALD, baseEmeralds, extraEmeralds),
                out,
                maxUses,
                villagerXp,
                0.2F
            );
        }
    }
}
