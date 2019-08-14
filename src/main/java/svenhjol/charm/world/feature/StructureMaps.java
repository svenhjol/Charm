package svenhjol.charm.world.feature;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.entity.merchant.villager.VillagerTrades.ITrade;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;
import net.minecraftforge.common.ForgeConfigSpec;
import svenhjol.meson.Feature;
import svenhjol.meson.helper.ItemNBTHelper;

import javax.annotation.Nullable;
import java.util.*;

public class StructureMaps extends Feature
{
    public static ForgeConfigSpec.ConfigValue<Integer> tradeLevel;
    public static ForgeConfigSpec.ConfigValue<Integer> tradeXp;
    public static ForgeConfigSpec.ConfigValue<Integer> maxUses;
    public static ForgeConfigSpec.ConfigValue<Integer> generalMinCost;
    public static ForgeConfigSpec.ConfigValue<Integer> generalMaxCost;
    public static ForgeConfigSpec.ConfigValue<Integer> biomeMinCost;
    public static ForgeConfigSpec.ConfigValue<Integer> biomeMaxCost;

    public static List<StructureTrade> trades = new ArrayList<>();

    @Override
    public void configure()
    {
        super.configure();

        tradeLevel = builder
            .comment("The level at which a cartographer will trade structure maps.\n" +
            "Numbers correspond to villager level, starting at 1 for Novice.")
            .define("Trade level", 3);
        tradeXp = builder
            .comment("The amount of experience awarded to the villager upon selling.")
            .define("Villager experience awarded", 5);
        maxUses = builder
            .comment("Maximum number the trade can be used before it locks.")
            .define("Maximum trades", 1);
        generalMinCost = builder
            .comment("Minimum emerald cost of a general structure map.")
            .define("General map minimum cost", 4);
        generalMaxCost = builder
            .comment("Maximum emerald cost of a general structure map.")
            .define("General map maximum cost", 7);
        biomeMinCost = builder
            .comment("Minimum emerald cost of a biome-specific structure map.")
            .define("Biome-specific map minimum cost", 16);
        biomeMaxCost = builder
            .comment("Maximum emerald cost of a biome-specific structure map.")
            .define("Biome-specific map maximum cost", 22);
    }

    @Override
    public void init()
    {
        super.init();

        int bmin = biomeMinCost.get();
        int bmax = biomeMaxCost.get();
        int gmin = generalMinCost.get();
        int gmax = generalMaxCost.get();

        /* @todo make structures configurable in config */
        trades.add(new StructureTrade("Desert_Pyramid").setColor(0x866600).setCost(bmin, bmax));
        trades.add(new StructureTrade("Igloo").setColor(0xA0C0FF).setCost(bmin, bmax));
        trades.add(new StructureTrade("Jungle_Pyramid").setColor(0x20B020).setCost(bmin, bmax));
        trades.add(new StructureTrade("Mineshaft").setColor(0x774400).setCost(gmin, gmax));
        trades.add(new StructureTrade("Ocean_Ruin").setColor(0x0000FF).setCost(gmin, gmax));
        trades.add(new StructureTrade("Pillager_Outpost").setColor(0xFF0033).setCost(gmin, gmax));
        trades.add(new StructureTrade("Shipwreck").setColor(0x990000).setCost(gmin, gmax));
        trades.add(new StructureTrade("Swamp_Hut").setColor(0x107000).setCost(bmin, bmax));
        trades.add(new StructureTrade("Village").setColor(0xCC2200).setCost(gmin, gmax));
    }

    @Override
    public void registerTrades(Int2ObjectMap<List<ITrade>> trades, VillagerProfession profession)
    {
        super.registerTrades(trades, profession);
        VillagerStructureMapTrade trade = new VillagerStructureMapTrade(maxUses.get(), tradeXp.get());

        if (Objects.requireNonNull(profession.getRegistryName()).toString().equals("minecraft:cartographer")) {
            trades.forEach((key, val) -> {
                if (key.equals(tradeLevel.get())) val.add(trade);
            });
        }
    }

    static class VillagerStructureMapTrade implements VillagerTrades.ITrade {
        private final MapDecoration.Type targetType;
        private final int maxUses;
        private final int tradeXp;
        private final float multiplier;

        public VillagerStructureMapTrade(int maxUses, int tradeXp)
        {
            this.multiplier = 0.2F;
            this.targetType = MapDecoration.Type.TARGET_X;
            this.maxUses = maxUses;
            this.tradeXp = tradeXp;
        }

        @Nullable
        public MerchantOffer getOffer(Entity merchant, Random rand)
        {
            StructureTrade structure = trades.get(rand.nextInt(trades.size()));
            World world = merchant.world;
            BlockPos pos = world.findNearestStructure(structure.name, new BlockPos(merchant), 100, true);
            if (pos != null) {

                // generate the map
                ItemStack stack = FilledMapItem.setupNewMap(world, pos.getX(), pos.getZ(), (byte)2, true, true);
                FilledMapItem.renderBiomePreviewMap(world, stack);
                MapData.addTargetDecoration(stack, pos, "+", this.targetType);
                stack.setDisplayName(new TranslationTextComponent("map.charm." + structure.name.toLowerCase(Locale.ENGLISH)));

                // set map color based on structure
                CompoundNBT tag = ItemNBTHelper.getCompound(stack, "display");
                tag.putInt("MapColor", structure.color);
                ItemNBTHelper.setCompound(stack, "display", tag);

                ItemStack in1 = new ItemStack(Items.EMERALD, rand.nextInt(structure.max - structure.min) + structure.min);
                ItemStack in2 = new ItemStack(Items.COMPASS);
                return new MerchantOffer(in1, in2, stack, maxUses, tradeXp, multiplier);
            } else {
                return null;
            }
        }
    }

    public class StructureTrade
    {
        public String name;
        public int dimension;
        public int color;
        public int min;
        public int max;

        public StructureTrade(String name)
        {
            this(name, 0, 0);
        }

        public StructureTrade(String name, int color)
        {
            this(name, color, 0);
        }

        public StructureTrade(String name, int color, int dimension)
        {
            this.dimension = dimension;
            this.name = name;
            this.color = color;
        }

        public StructureTrade setCost(int min, int max)
        {
            this.min = min;
            this.max = max;
            return this;
        }

        public StructureTrade setColor(int color)
        {
            this.color = color;
            return this;
        }
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
