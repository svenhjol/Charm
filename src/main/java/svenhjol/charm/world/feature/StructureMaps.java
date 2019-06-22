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
import java.util.List;
import java.util.Random;

public class StructureMaps extends Feature
{
    public static List<Structure> structures = new ArrayList<>();

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        structures.add(new Structure(StructureType.MINESHAFT, 0x774400));
        structures.add(new Structure(StructureType.VILLAGE, 0xFF2200));
        structures.add(new Structure(StructureType.IGLOO, 0xE0E0FF));
        structures.add(new Structure(StructureType.DESERT_PYRAMID, 0xFFEF60));
        structures.add(new Structure(StructureType.JUNGLE_PYRAMID, 0x20B020));
        structures.add(new Structure(StructureType.SWAMP_HUT, 0x107000));
    }

    @SubscribeEvent
    public void onRegisterVillagers(RegistryEvent.Register<VillagerProfession> event)
    {
        VillagerCareer cartographer = VillagerRegistry.cartographer;
        cartographer.addTrade(3, new StructureMapTrade());
    }

    public static ItemStack createMap(World world, BlockPos pos, Structure structure)
    {
        BlockPos offset = pos.offset(EnumFacing.random(world.rand), 250);
        BlockPos structurePos = WorldHelper.getNearestStructure(world, offset, structure.type);
        if (structurePos == null) return ItemStack.EMPTY;

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
            ItemStack map = createMap(merchant.getWorld(), merchant.getPos(), structures.get(random.nextInt(structures.size())));
            int emeraldCount = random.nextInt(3) + 3;
            recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, emeraldCount), new ItemStack(Items.COMPASS), map, 0, 1));
        }
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
