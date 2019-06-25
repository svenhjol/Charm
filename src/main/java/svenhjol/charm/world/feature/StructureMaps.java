package svenhjol.charm.world.feature;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager.ITradeList;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;
import svenhjol.meson.Feature;
import svenhjol.meson.helper.ItemNBTHelper;
import svenhjol.meson.helper.WorldHelper;
import svenhjol.meson.helper.WorldHelper.StructureType;
import svenhjol.meson.registry.VillagerRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class StructureMaps extends Feature
{
    public static int tradeLevel;
    public static int biomeMinCost;
    public static int biomeMaxCost;
    public static int generalMinCost;
    public static int generalMaxCost;
    public static List<String> biomeStructures = new ArrayList<>();
    public static List<String> generalStructures = new ArrayList<>();
    public static List<Structure> structures = new ArrayList<>();

    @Override
    public String getDescription()
    {
        return "Cartographers sell Structure Maps that can be used to find overworld structures.";
    }

    @Override
    public void setupConfig()
    {
        biomeStructures = Arrays.asList(
            propStringList(
                "Biome-specific structures",
                "List of biome-specific structures that are available as map locations.",
                new String[] {
                    StructureType.DESERT_PYRAMID.getName(),
                    StructureType.IGLOO.getName(),
                    StructureType.JUNGLE_PYRAMID.getName(),
                    StructureType.SWAMP_HUT.getName(),
                })
        );
        generalStructures = Arrays.asList(
            propStringList(
                "General structures",
                "List of general structures that are available as map locations.",
                new String[] {
                    StructureType.MINESHAFT.getName(),
                    StructureType.VILLAGE.getName()
                }
            )
        );

        tradeLevel = propInt("Trade level",
            "The level at which a cartographer will be unlocked for trading structure maps.",
            3);
        generalMinCost = propInt("General structures minimum cost",
            "Minimum emerald cost of a general structure map.",
            4);
        generalMaxCost = propInt("General structures maximum cost",
            "Maximum emerald cost of a general structure map.",
            7);
        biomeMinCost = propInt("Biome-specific minimum cost",
            "Minimum emerald cost of a biome-specific structure map.",
            16);
        biomeMaxCost = propInt("Biome-specific maximum cost",
            "Maximum emerald cost of a biome-specific structure map.",
            22);
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        if (generalStructures.contains(StructureType.MINESHAFT.getName())) structures.add(new Structure(StructureType.MINESHAFT, 0x774400));
        if (generalStructures.contains(StructureType.VILLAGE.getName())) structures.add(new Structure(StructureType.VILLAGE, 0xCC2000));
        if (biomeStructures.contains(StructureType.IGLOO.getName())) structures.add(new Structure(StructureType.IGLOO, 0xA0C0FF));
        if (biomeStructures.contains(StructureType.DESERT_PYRAMID.getName())) structures.add(new Structure(StructureType.DESERT_PYRAMID, 0x866600));
        if (biomeStructures.contains(StructureType.JUNGLE_PYRAMID.getName())) structures.add(new Structure(StructureType.JUNGLE_PYRAMID, 0x20B020));
        if (biomeStructures.contains(StructureType.SWAMP_HUT.getName())) structures.add(new Structure(StructureType.SWAMP_HUT, 0x107000));
    }

    @SubscribeEvent
    public void onRegisterVillagers(RegistryEvent.Register<VillagerProfession> event)
    {
        VillagerCareer cartographer = VillagerRegistry.cartographer;
        cartographer.addTrade(tradeLevel, new StructureMapTrade());
    }

    public static ItemStack createMap(World world, BlockPos pos, Structure structure)
    {
        BlockPos offset = pos.offset(EnumFacing.random(world.rand), 250);
        BlockPos structurePos = WorldHelper.getNearestStructure(world, offset, structure.type);
        if (structurePos == null) return new ItemStack(Items.MAP, 2);

        int id = world.getUniqueDataId("map");
        ItemStack stack = new ItemStack(Items.FILLED_MAP, 1, id);
        stack.setTranslatableName("charm.map." + structure.type.getName());

        NBTTagCompound tag = ItemNBTHelper.getCompound(stack, "display");
        tag.setInteger("MapColor", structure.color);
        ItemNBTHelper.setCompound(stack, "display", tag);

        String mapId = "map_" + id;
        MapData mapData = new MapData(mapId);
        world.setData(mapId, mapData);
        mapData.scale = 2;
        mapData.xCenter = structurePos.getX() + (int)((Math.random() - 0.5) * 200);
        mapData.zCenter = structurePos.getZ() + (int)((Math.random() - 0.5) * 200);
        mapData.dimension = structure.dimension;
        mapData.trackingPosition = true;
        mapData.unlimitedTracking = true;

        ItemMap.renderBiomePreviewMap(world, stack);
        MapData.addTargetDecoration(stack, structurePos, "+", MapDecoration.Type.TARGET_X);

        return stack;
    }

    public class Structure
    {
        public StructureType type;
        public int dimension = 0;
        public int color = 0;

        public Structure(StructureType type)
        {
            this.type = type;
        }

        public Structure(StructureType type, int color)
        {
            this.type = type;
            this.color = color;
        }

        public Structure(StructureType type, int color, int dimension)
        {
            this.type = type;
            this.dimension = dimension;
            this.color = color;
        }
    }

    public class StructureMapTrade implements ITradeList
    {
        @Override
        public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random)
        {
            if (structures.isEmpty()) return;

            Structure structure = structures.get(random.nextInt(structures.size()));
            ItemStack map = createMap(merchant.getWorld(), merchant.getPos(), structure);

            int emeraldCount;
            if (generalStructures.contains(structure.type.getName())) {
                emeraldCount = random.nextInt(generalMaxCost - generalMinCost) + generalMinCost;
            } else {
                emeraldCount = random.nextInt(biomeMaxCost - biomeMinCost) + biomeMinCost;
            }
            recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, emeraldCount), new ItemStack(Items.COMPASS), map, 0, 1));
        }
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
