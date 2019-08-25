package svenhjol.charm.world.module;

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
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.ItemNBTHelper;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

import javax.annotation.Nullable;
import java.util.*;

@Module(mod = Charm.MOD_ID, category = CharmCategories.WORLD, hasSubscriptions = true)
public class StructureMaps extends MesonModule
{
    @Config(name = "Trade Level", description = "The level at which a cartographer will trade structure maps.\n" +
            "Numbers correspond to villager level, starting at 1 for Novice.")
    public static int tradeLevel = 3;

    @Config(name = "Villager experience awarded", description = "The amount of experience awarded to the villager upon selling.")
    public static int tradeXp = 5;

    @Config(name = "Maximum Trades", description = "Maximum number the trade can be used before it locks.")
    public static int maxTrades = 1;

    @Config(name = "General map minimum cost", description = "Minimum emerald cost of a general structure map.")
    public static int generalMinCost = 4;

    @Config(name = "General map maximum cost", description = "Maximum emerald cost of a general structure map.")
    public static int generalMaxCost = 7;

    @Config(name = "Biome-specific map minimum cost", description = "Minimum emerald cost of a biome-specific structure map.")
    public static int biomeMinCost = 16;

    @Config(name = "Biome-specific map maximum cost", description = "Maximum emerald cost of a biome-specific structure map.")
    public static int biomeMaxCost = 22;

    public static List<StructureTrade> trades = new ArrayList<>();

    @Override
    public void init()
    {
        int bmin = biomeMinCost;
        int bmax = biomeMaxCost;
        int gmin = generalMinCost;
        int gmax = generalMaxCost;

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
        StructureMapForEmeraldsTrade trade = new StructureMapForEmeraldsTrade();
        if (profession.getRegistryName() == null) return;

        if (profession.getRegistryName().getPath().equals("cartographer")) {
            trades.get(tradeLevel).add(trade);
        }
    }

    static class StructureMapForEmeraldsTrade implements VillagerTrades.ITrade
    {
        private final MapDecoration.Type targetType = MapDecoration.Type.TARGET_X;
        private final int maxUses = StructureMaps.maxTrades;
        private final int tradeXp = StructureMaps.tradeXp;
        private final float multiplier = 0.2F;

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
}
