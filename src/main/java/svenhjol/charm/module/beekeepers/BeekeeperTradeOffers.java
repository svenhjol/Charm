package svenhjol.charm.module.beekeepers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import svenhjol.charm.helper.TagHelper;
import svenhjol.charm.helper.TextHelper;
import svenhjol.charm.helper.VillagerHelper.SingleItemTypeTrade;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BeekeeperTradeOffers {
    public static class EmeraldsForFlowers extends SingleItemTypeTrade {
        @Nullable
        @Override
        public MerchantOffer getOffer(Entity entity, Random random) {
            List<Item> flowers = TagHelper.getItemValues(ItemTags.FLOWERS);
            ItemLike item;

            var size = Registry.ITEM.getTagOrEmpty(ItemTags.FLOWERS);
            for (Holder<Item> holder : size) {
                flowers.add(holder.value());
            }

            if (flowers.isEmpty()) {
                item = Items.DANDELION.asItem();
            } else {
                // No wither roses pls.
                var filtered = flowers.stream().filter(f -> f != Items.WITHER_ROSE).toList();
                item = filtered.get(random.nextInt(filtered.size()));
            }

            setInput(item, random.nextInt(3) + 13);
            setOutput(Items.EMERALD, 1);
            return super.getOffer(entity, random);
        }
    }

    public static class EmeraldsForCharcoal extends SingleItemTypeTrade {
        @Nullable
        @Override
        public MerchantOffer getOffer(Entity entity, Random random) {
            setInput(Items.CHARCOAL, random.nextInt(3) + 13);
            setOutput(Items.EMERALD, 1);
            return super.getOffer(entity, random);
        }
    }

    public static class EmeraldsForHoneycomb extends SingleItemTypeTrade {
        @Nullable
        @Override
        public MerchantOffer getOffer(Entity entity, Random random) {
            setInput(Items.HONEYCOMB, 10);
            setOutput(Items.EMERALD, random.nextInt(2) + 1);
            return super.getOffer(entity, random);
        }
    }

    public static class BottlesForEmerald extends SingleItemTypeTrade {
        @Nullable
        @Override
        public MerchantOffer getOffer(Entity entity, Random random) {
            setInput(Items.EMERALD, 1);
            setOutput(Items.GLASS_BOTTLE, random.nextInt(4) + 2);
            return super.getOffer(entity, random);
        }
    }

    public static class CandlesForEmeralds extends SingleItemTypeTrade {
        @Nullable
        @Override
        public MerchantOffer getOffer(Entity entity, Random random) {
            setInput(Items.EMERALD, 3);

            var candles = TagHelper.getItemValues(ItemTags.CANDLES);
            var candle = TagHelper.getRandomElement(candles, Items.CANDLE, random);
            setOutput(candle, 1);

            return super.getOffer(entity, random);
        }
    }

    public static class CampfireForEmerald extends SingleItemTypeTrade {
        @Nullable
        @Override
        public MerchantOffer getOffer(Entity entity, Random random) {
            setInput(Items.EMERALD, 1);
            setOutput(Items.CAMPFIRE, 1);
            return super.getOffer(entity, random);
        }
    }

    public static class LeadForEmeralds extends SingleItemTypeTrade {
        @Nullable
        @Override
        public MerchantOffer getOffer(Entity entity, Random random) {
            setInput(Items.EMERALD, 6);
            setOutput(Items.LEAD, 1);
            return super.getOffer(entity, random);
        }
    }

    public static class HoneyBottlesForEmeralds implements VillagerTrades.ItemListing {
        @Nullable
        @Override
        public MerchantOffer getOffer(Entity entity, Random random) {
            int count = random.nextInt(2) + 1;
            ItemStack in1 = new ItemStack(Items.EMERALD, count);
            ItemStack out = new ItemStack(Items.HONEY_BOTTLE, count);
            return new MerchantOffer(in1, out, 20, 2, 0.05F);
        }
    }

    public static class PopulatedBeehiveForEmeralds implements VillagerTrades.ItemListing {
        @Nullable
        @Override
        public MerchantOffer getOffer(Entity entity, Random random) {
            int count = random.nextInt(14) + 21;
            ItemStack in1 = new ItemStack(Items.EMERALD, count);
            ItemStack out = new ItemStack(Items.BEEHIVE);

            BeehiveBlockEntity blockEntity = new BeehiveBlockEntity(BlockPos.ZERO, Blocks.BEEHIVE.defaultBlockState());

            for (int i = 0; i < 1; i++) {
                Bee bee = new Bee(EntityType.BEE, entity.level);
                blockEntity.addOccupantWithPresetTicks(bee, false, 0);
            }

            CompoundTag beesTag = new CompoundTag();
            CompoundTag honeyTag = new CompoundTag();
            beesTag.put("Bees", blockEntity.writeBees());
            honeyTag.putInt("honey_level", 0);
            out.addTagElement("BlockEntityTag", beesTag);
            out.addTagElement("BlockStateTag", honeyTag);
            out.setHoverName(TextHelper.translatable("item.charm.populated_beehive"));

            return new MerchantOffer(in1, out, 1, 10, 0.2F);
        }
    }
}
