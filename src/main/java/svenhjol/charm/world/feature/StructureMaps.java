package svenhjol.charm.world.feature;

import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;
import svenhjol.meson.Feature;
import svenhjol.meson.iface.IMesonEnum;

import java.util.ArrayList;
import java.util.List;

public class StructureMaps extends Feature
{
    public static List<StructureType> structures = new ArrayList<>();

    public enum StructureType implements IMesonEnum
    {
        VILLAGE,
        MINESHAFT,
        SWAMP_HUT,
        DESERT_PYRAMID,
        JUNGLE_PYRAMID,
        IGLOO,
        STRONGHOLD,
        MANSION
    }

    @Override
    public void configure()
    {
        super.configure();

    }

    public static ItemStack createMap(World world, BlockPos pos, Structure structure)
    {
        BlockPos offset = pos.offset(Direction.random(world.rand), 250);
        BlockPos structurePos = world.findNearestStructure(structure.type.getCapitalizedName(), pos, 100, true);
        if (structurePos == null) return new ItemStack(Items.MAP, 2);

        ItemStack stack = FilledMapItem.setupNewMap(world, pos.getX(), pos.getZ(), (byte)2, true, true);
        FilledMapItem.renderBiomePreviewMap(world, stack);
        MapData.addTargetDecoration(stack, pos, "+", MapDecoration.Type.TARGET_X);
        stack.setDisplayName(new TranslationTextComponent("charm.map." + structure.type.getName()));

        return stack;
    }

//    public class StructureMapTrade implements VillagerTrades.ITrade
//    {
//        public StructureMapTrade()
//        {
//
//        }
//    }

    public class Structure
    {
        public StructureType type;
        public int dimension;
        public int color;

        public Structure(StructureType type)
        {
            this(type, 0, 0);
        }

        public Structure(StructureType type, int color)
        {
            this(type, color, 0);
        }

        public Structure(StructureType type, int color, int dimension)
        {
            this.dimension = dimension;
            this.type = type;
            this.color = color;
        }
    }
}
