package svenhjol.charm.module.lumberjacks;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import svenhjol.charm.Charm;
import svenhjol.charm.enums.IWoodMaterial;
import svenhjol.charm.helper.DecorationHelper;
import svenhjol.charm.helper.VillagerHelper.SingleItemTypeTrade;
import svenhjol.charm.module.bookcases.Bookcases;
import svenhjol.charm.module.variant_barrels.VariantBarrels;
import svenhjol.charm.module.variant_ladders.VariantLadders;
import svenhjol.charm.module.woodcutters.Woodcutters;

import javax.annotation.Nullable;
import java.util.*;

public class LumberjackTradeOffers {

    // TIER 1

    public static class EmeraldsForOverworldLogs extends SingleItemTypeTrade {
        @Nullable
        @Override
        public MerchantOffer getOffer(Entity entity, @Nullable Random random) {
            experience = 2;
            List<ItemLike> logs = new ArrayList<>(Arrays.asList(
                Blocks.ACACIA_LOG,
                Blocks.BIRCH_LOG,
                Blocks.DARK_OAK_LOG,
                Blocks.JUNGLE_LOG,
                Blocks.OAK_LOG,
                Blocks.SPRUCE_LOG,
                Blocks.STRIPPED_ACACIA_LOG,
                Blocks.STRIPPED_BIRCH_LOG,
                Blocks.STRIPPED_DARK_OAK_LOG,
                Blocks.STRIPPED_JUNGLE_LOG,
                Blocks.STRIPPED_OAK_LOG,
                Blocks.STRIPPED_SPRUCE_LOG
            ));
            setInput(logs.get(random.nextInt(logs.size())), 8);
            setOutput(Items.EMERALD, 1);
            return super.getOffer(entity, random);
        }
    }

    public static class CommonSaplingsForEmeralds extends SingleItemTypeTrade {
        @Nullable
        @Override
        public MerchantOffer getOffer(Entity entity, @Nullable Random random) {
            experience = 2;
            List<Item> saplings = Arrays.asList(Items.OAK_SAPLING, Items.BIRCH_SAPLING, Items.SPRUCE_SAPLING);
            setInput(Items.EMERALD, 1);
            setOutput(saplings.get(random.nextInt(saplings.size())), 1);
            return super.getOffer(entity, random);
        }
    }

    public static class LaddersForEmeralds extends SingleItemTypeTrade {
        @Nullable
        @Override
        public MerchantOffer getOffer(Entity entity, @Nullable Random random) {
            experience = 2;
            ItemLike item;
            if (Charm.LOADER.isEnabled("charm:variant_ladders")) {
                IWoodMaterial variant = DecorationHelper.getRandomVariantMaterial(random);
                item = VariantLadders.LADDER_BLOCKS.get(variant);
            } else {
                item = Items.LADDER;
            }
            setInput(Items.EMERALD, 1);
            setOutput(item, 1);
            return super.getOffer(entity, random);
        }
    }

    // TIER 2

    public static class EmeraldsForBones extends SingleItemTypeTrade {
        @Nullable
        @Override
        public MerchantOffer getOffer(Entity entity, @Nullable Random random) {
            experience = 5;
            setInput(Items.BONE, random.nextInt(2) + 23);
            setOutput(Items.EMERALD, 1);
            return super.getOffer(entity, random);
        }
    }

    public static class BedForEmeralds extends SingleItemTypeTrade {
        @Nullable
        @Override
        public MerchantOffer getOffer(Entity entity, @Nullable Random random) {
            experience = 7;
            List<Item> beds = ItemTags.BEDS.getValues();
            setInput(Items.EMERALD, 3);
            setOutput(beds.get(random.nextInt(beds.size())), 1);
            return super.getOffer(entity, random);
        }
    }

    public static class FencesForEmeralds extends SingleItemTypeTrade {
        @Nullable
        @Override
        public MerchantOffer getOffer(Entity entity, @Nullable Random random) {
            experience = 6;
            List<ItemLike> doors = random.nextFloat() < 0.5F ? new ArrayList<>(ItemTags.WOODEN_FENCES.getValues()) : new ArrayList<>(BlockTags.FENCE_GATES.getValues());
            setInput(Items.EMERALD, 1);
            setOutput(doors.get(random.nextInt(doors.size())), 1);
            return super.getOffer(entity, random);
        }
    }

    // TIER 3

    public static class EmeraldsForStems extends SingleItemTypeTrade {
        @Nullable
        @Override
        public MerchantOffer getOffer(Entity entity, @Nullable Random random) {
            List<Item> stems;
            experience = 10;

            if (random.nextFloat() < 0.5F) {
                stems = ItemTags.CRIMSON_STEMS.getValues();
            } else {
                stems = ItemTags.WARPED_STEMS.getValues();
            }

            setInput(stems.get(random.nextInt(stems.size())), 7);
            setOutput(Items.EMERALD, 1);
            return super.getOffer(entity, random);
        }
    }

    public static class BarkForLogs implements VillagerTrades.ItemListing {
        @Nullable
        @Override
        public MerchantOffer getOffer(Entity entity, @Nullable Random random) {
            Map<Block, Block> wood = new HashMap<>();

            wood.put(Blocks.ACACIA_LOG, Blocks.ACACIA_WOOD);
            wood.put(Blocks.BIRCH_LOG, Blocks.BIRCH_WOOD);
            wood.put(Blocks.DARK_OAK_LOG, Blocks.DARK_OAK_WOOD);
            wood.put(Blocks.JUNGLE_LOG, Blocks.JUNGLE_WOOD);
            wood.put(Blocks.OAK_LOG, Blocks.OAK_WOOD);
            wood.put(Blocks.SPRUCE_LOG, Blocks.SPRUCE_WOOD);

            int count = random.nextInt(5) + 12;
            int index = random.nextInt(wood.size());
            List<Block> logs = new ArrayList<>(wood.keySet());

            ItemLike in1 = logs.get(index);
            ItemLike in2 = Items.EMERALD;
            ItemLike out = wood.get(logs.get(index));

            return new MerchantOffer(new ItemStack(in1, count), new ItemStack(in2, 1), new ItemStack(out, count), 5, 10, 0.05F);
        }
    }

    public static class UncommonSaplingsForEmeralds extends SingleItemTypeTrade {
        @Nullable
        @Override
        public MerchantOffer getOffer(Entity entity, @Nullable Random random) {
            experience = 10;
            List<Item> saplings = Arrays.asList(Items.ACACIA_SAPLING, Items.DARK_OAK_SAPLING, Items.JUNGLE_SAPLING);
            setInput(Items.EMERALD, 3);
            setOutput(saplings.get(random.nextInt(saplings.size())), 1);
            return super.getOffer(entity, random);
        }
    }

    public static class DoorsForEmeralds extends SingleItemTypeTrade {
        @Nullable
        @Override
        public MerchantOffer getOffer(Entity entity, @Nullable Random random) {
            experience = 10;
            List<Item> doors = random.nextFloat() < 0.5F ? ItemTags.WOODEN_DOORS.getValues() : ItemTags.WOODEN_TRAPDOORS.getValues();
            setInput(Items.EMERALD, 1);
            setOutput(doors.get(random.nextInt(doors.size())), 1);
            return super.getOffer(entity, random);
        }
    }

    // TIER 4

    public static class BarrelForEmeralds extends SingleItemTypeTrade {
        @Nullable
        @Override
        public MerchantOffer getOffer(Entity entity, @Nullable Random random) {
            experience = 15;
            ItemLike out;
            setInput(Items.EMERALD, 4);

            if (Charm.LOADER.isEnabled("charm:variant_barrels")) {
                IWoodMaterial variant = DecorationHelper.getRandomVariantMaterial(random);
                out = VariantBarrels.BARREL_BLOCKS.get(variant);
            } else {
                out = Blocks.BARREL;
            }

            setOutput(out, random.nextInt(2) + 1);
            return super.getOffer(entity, random);
        }
    }

    /**
     * @deprecated Log inputs can be reduced to 1, making this trade too OP.
     */
    @Deprecated
    public static class PlanksForLogs implements VillagerTrades.ItemListing {
        @Nullable
        @Override
        public MerchantOffer getOffer(Entity entity, @Nullable Random random) {
            Map<Block, Block> planks = new HashMap<>();

            planks.put(Blocks.ACACIA_LOG, Blocks.ACACIA_PLANKS);
            planks.put(Blocks.BIRCH_LOG, Blocks.BIRCH_PLANKS);
            planks.put(Blocks.CRIMSON_STEM, Blocks.CRIMSON_PLANKS);
            planks.put(Blocks.DARK_OAK_LOG, Blocks.DARK_OAK_PLANKS);
            planks.put(Blocks.JUNGLE_LOG, Blocks.JUNGLE_PLANKS);
            planks.put(Blocks.OAK_LOG, Blocks.OAK_PLANKS);
            planks.put(Blocks.SPRUCE_LOG, Blocks.SPRUCE_PLANKS);
            planks.put(Blocks.WARPED_STEM, Blocks.WARPED_PLANKS);

            int count = random.nextInt(8) + 3;
            int index = random.nextInt(planks.size());
            List<Block> logs = new ArrayList<>(planks.keySet());

            ItemLike in1 = logs.get(index);
            ItemLike in2 = Items.EMERALD;
            ItemLike out = planks.get(logs.get(index));

            return new MerchantOffer(new ItemStack(in1, count), new ItemStack(in2, 1), new ItemStack(out, count * 6), 5, 15, 0.05F);
        }
    }

    public static class MusicBlocksForLogs extends SingleItemTypeTrade {
        @Nullable
        @Override
        public MerchantOffer getOffer(Entity entity, @Nullable Random random) {
            experience = 15;
            if (random.nextFloat() < 0.5F) {
                setOutput(Blocks.JUKEBOX, 1);
            } else {
                setOutput(Blocks.NOTE_BLOCK, 1);
            }

            setInput(Items.EMERALD, 8);
            return super.getOffer(entity, random);
        }
    }

    // TIER 5

    public static class BookcaseForEmeralds extends SingleItemTypeTrade {
        @Nullable
        @Override
        public MerchantOffer getOffer(Entity entity, @Nullable Random random) {
            experience = 30;
            IWoodMaterial variant = DecorationHelper.getRandomVariantMaterial(random);
            setInput(Items.EMERALD, 8);
            setOutput(Bookcases.BOOKCASE_BLOCKS.get(variant), 1);
            return super.getOffer(entity, random);
        }
    }

    public static class WorkstationForEmeralds extends SingleItemTypeTrade {
        @Nullable
        @Override
        public MerchantOffer getOffer(Entity entity, @Nullable Random random) {
            experience = 30;

            List<Block> workstations = new ArrayList<>(Arrays.asList(
                Blocks.FLETCHING_TABLE,
                Blocks.CRAFTING_TABLE,
                Blocks.CARTOGRAPHY_TABLE,
                Blocks.COMPOSTER,
                Blocks.LOOM
            ));

            if (Charm.LOADER.isEnabled("charm:woodcutters"))
                workstations.add(Woodcutters.WOODCUTTER);

            IWoodMaterial variant = DecorationHelper.getRandomVariantMaterial(random);
            setInput(Items.EMERALD, 5);
            setOutput(workstations.get(random.nextInt(workstations.size())), 1);
            return super.getOffer(entity, random);
        }
    }
}
