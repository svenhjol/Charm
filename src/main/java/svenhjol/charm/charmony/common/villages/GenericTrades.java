package svenhjol.charm.charmony.common.villages;

import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;
import svenhjol.charm.charmony.Charmony;
import svenhjol.charm.charmony.Log;
import svenhjol.charm.charmony.common.helper.TagHelper;

import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("unused")
public final class GenericTrades {
    private static final Log LOGGER = new Log(Charmony.ID, "GenericTrades");

    public static class EmeraldsForTag<T extends ItemLike> implements VillagerTrades.ItemListing {
        private final int villagerXp;
        private final int baseCost;
        private final int extraCost;
        private final int baseEmeralds;
        private final int extraEmeralds;
        private final int maxUses;
        private final TagKey<T> tag;

        public EmeraldsForTag(TagKey<T> tag, int baseCost, int baseEmeralds, int villagerXp, int maxUses) {
            this(tag, baseCost, 0, baseEmeralds, 0, villagerXp, maxUses);
        }

        public EmeraldsForTag(TagKey<T> tag, int baseCost, int extraCost, int baseEmeralds, int extraEmeralds, int villagerXp, int maxUses) {
            this.tag = tag;
            this.villagerXp = villagerXp;
            this.extraCost = extraCost;
            this.baseCost = baseCost;
            this.maxUses = maxUses;
            this.baseEmeralds = baseEmeralds;
            this.extraEmeralds = extraEmeralds;
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(Entity merchant, RandomSource random) {
            var level = merchant.level();
            var blocks = TagHelper.getValues(level.registryAccess()
                .registryOrThrow(tag.registry()), tag);

            if (blocks.isEmpty()) {
                return null;
            }

            return new MerchantOffer(
                getCostFromList(random, blocks, baseCost, extraCost),
                getStack(random, Items.EMERALD, baseEmeralds, extraEmeralds),
                maxUses,
                villagerXp,
                0.05F
            );
        }
    }

    public static class EmeraldsForTwoTags<T extends ItemLike, U extends ItemLike> implements VillagerTrades.ItemListing {
        private final int villagerXp;
        private final int baseCost;
        private final int extraCost;
        private final int baseEmeralds;
        private final int extraEmeralds;
        private final int maxUses;
        private final TagKey<T> tag1;
        private final TagKey<U> tag2;

        public EmeraldsForTwoTags(TagKey<T> tag1, TagKey<U> tag2, int baseCost, int baseEmeralds, int villagerXp, int maxUses) {
            this(tag1, tag2, baseCost, 0, baseEmeralds, 0, villagerXp, maxUses);
        }

        public EmeraldsForTwoTags(TagKey<T> tag1, TagKey<U> tag2, int baseCost, int extraCost, int baseEmeralds, int extraEmeralds, int villagerXp, int maxUses) {
            this.tag1 = tag1;
            this.tag2 = tag2;
            this.villagerXp = villagerXp;
            this.extraCost = extraCost;
            this.baseCost = baseCost;
            this.maxUses = maxUses;
            this.baseEmeralds = baseEmeralds;
            this.extraEmeralds = extraEmeralds;
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(Entity merchant, RandomSource random) {
            var level = merchant.level();

            var values1 = TagHelper.getValues(level.registryAccess().registryOrThrow(tag1.registry()), tag1);
            var values2 = TagHelper.getValues(level.registryAccess().registryOrThrow(tag2.registry()), tag2);
            if (values1.isEmpty() || values2.isEmpty()) return null;

            return new MerchantOffer(
                getCostFromList(random, values1, baseCost, extraCost),
                getCostFromList(random, values2, baseCost, extraCost),
                getStack(random, Items.EMERALD, baseEmeralds, extraEmeralds),
                maxUses,
                villagerXp,
                0.05F
            );
        }
    }

    public static class EmeraldsForItems implements VillagerTrades.ItemListing {
        private final int villagerXp;
        private final int baseCost;
        private final int extraCost;
        private final int baseEmeralds;
        private final int extraEmeralds;
        private final int maxUses;
        private final ItemLike itemLike;

        public EmeraldsForItems(ItemLike itemLike, int baseCost, int baseEmeralds, int villagerXp, int maxUses) {
            this(itemLike, baseCost, 0, baseEmeralds, 0, villagerXp, maxUses);
        }

        public EmeraldsForItems(ItemLike itemLike, int baseCost, int extraCost, int baseEmeralds, int extraEmeralds, int villagerXp, int maxUses) {
            this.itemLike = itemLike;
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
            return new MerchantOffer(
                getCost(random, itemLike, baseCost, extraCost),
                getStack(random, Items.EMERALD, baseEmeralds, extraEmeralds),
                maxUses,
                villagerXp,
                0.05F
            );
        }
    }

    public static class TagForEmeralds<T extends ItemLike> implements VillagerTrades.ItemListing {
        private final int villagerXp;
        private final int baseItems;
        private final int extraItems;
        private final int baseEmeralds;
        private final int extraEmeralds;
        private final int maxUses;
        private final TagKey<T> tag;

        public TagForEmeralds(TagKey<T> tag, int baseEmeralds, int baseItems, int villagerXp, int maxUses) {
            this(tag, baseEmeralds, 0, baseItems, 0, villagerXp, maxUses);
        }

        public TagForEmeralds(TagKey<T> tag, int baseEmeralds, int extraEmeralds, int baseItems, int extraItems, int villagerXp, int maxUses) {
            this.tag = tag;
            this.baseItems = baseItems;
            this.extraItems = extraItems;
            this.baseEmeralds = baseEmeralds;
            this.extraEmeralds = extraEmeralds;
            this.villagerXp = villagerXp;
            this.maxUses = maxUses;
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(Entity merchant, RandomSource random) {
            var level = merchant.level();
            var blocks = TagHelper.getValues(level.registryAccess()
                .registryOrThrow(tag.registry()), tag);

            return new MerchantOffer(
                getCost(random, Items.EMERALD, baseEmeralds, extraEmeralds),
                getStackFromList(random, blocks, baseItems, extraItems),
                maxUses,
                villagerXp,
                0.05F
            );
        }
    }

    public static class ItemsForEmeralds implements VillagerTrades.ItemListing {
        private final int villagerXp;
        private final int baseItems;
        private final int extraItems;
        private final int baseEmeralds;
        private final int extraEmeralds;
        private final int maxUses;
        private final ItemLike itemLike;

        public ItemsForEmeralds(ItemLike itemLike, int baseEmeralds, int baseItems, int villagerXp, int maxUses) {
            this(itemLike, baseEmeralds, 0, baseItems, 0, villagerXp, maxUses);
        }

        public ItemsForEmeralds(ItemLike itemLike, int baseEmeralds, int extraEmeralds, int baseItems, int extraItems, int villagerXp, int maxUses) {
            this.itemLike = itemLike;
            this.baseItems = baseItems;
            this.extraItems = extraItems;
            this.baseEmeralds = baseEmeralds;
            this.extraEmeralds = extraEmeralds;
            this.villagerXp = villagerXp;
            this.maxUses = maxUses;
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(Entity merchant, RandomSource random) {
            return new MerchantOffer(
                getCost(random, Items.EMERALD, baseEmeralds, extraEmeralds),
                getStack(random, itemLike, baseItems, extraItems),
                maxUses,
                villagerXp,
                0.05F
            );
        }
    }

    public static class ItemsForItems implements VillagerTrades.ItemListing {
        private final int villagerXp;
        private final int baseInput;
        private final int extraInput;
        private final int baseOutput;
        private final int extraOutput;
        private final int maxUses;
        private final ItemLike inputItem;
        private final ItemLike outputItem;

        public ItemsForItems(ItemLike inputItem, ItemLike outputItem, int baseInput, int baseOutput, int villagerXp, int maxUses) {
            this(inputItem, outputItem, baseInput, 0, baseOutput, 0, villagerXp, maxUses);
        }

        public ItemsForItems(ItemLike inputItem, ItemLike outputItem, int baseInput, int extraInput, int baseOutput, int extraOutput, int villagerXp, int maxUses) {
            this.inputItem = inputItem;
            this.outputItem = outputItem;
            this.baseInput = baseInput;
            this.extraInput = extraInput;
            this.baseOutput = baseOutput;
            this.extraOutput = extraOutput;
            this.villagerXp = villagerXp;
            this.maxUses = maxUses;
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(Entity merchant, RandomSource random) {
            return new MerchantOffer(
                getCost(random, inputItem, baseInput, extraInput),
                getStack(random, outputItem, baseOutput, extraOutput),
                maxUses,
                villagerXp,
                0.05F
            );
        }
    }

    public static <T extends ItemLike> ItemStack getStackFromList(RandomSource random, List<T> list, int baseCount, int extraCount) {
        if (list.isEmpty()) LOGGER.die("Item list must be populated");
        return getStack(random, list.get(random.nextInt(list.size())), baseCount, extraCount);
    }

    public static <T extends ItemLike> ItemStack getCostFromList(RandomSource random, List<T> list, int baseCount, int extraCount) {
        if (list.isEmpty()) LOGGER.die("Item list must be populated");
        return getCost(random, list.get(random.nextInt(list.size())), baseCount, extraCount);
    }

    public static ItemStack getCost(ItemLike itemLike, int baseCost) {
        return new ItemStack(itemLike, baseCost);
    }

    public static ItemStack getCost(RandomSource random, ItemLike itemLike, int baseCount, int extraCount) {
        return new ItemStack(itemLike, baseCount + random.nextInt(extraCount + 1));
    }

    public static ItemStack getStack(RandomSource random, ItemLike itemLike, int baseCount, int extraCount) {
        return new ItemStack(itemLike, baseCount + random.nextInt(extraCount + 1));
    }
}
