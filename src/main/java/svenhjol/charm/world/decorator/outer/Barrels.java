package svenhjol.charm.world.decorator.outer;

import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmLootTables;
import svenhjol.charm.crafting.feature.Barrel;
import svenhjol.charm.crafting.feature.Composter;
import svenhjol.charm.crafting.tile.TileBarrel;
import svenhjol.charm.world.feature.VillageDecorations;
import svenhjol.meson.decorator.MesonOuterDecorator;

import java.util.List;
import java.util.Random;

public class Barrels extends MesonOuterDecorator
{
    public Barrels(World world, BlockPos pos, Random rand, List<ChunkPos> chunks)
    {
        super(world, pos, rand, chunks);
    }

    @Override
    public void generate()
    {
        if (!Charm.hasFeature(Barrel.class) && !Charm.hasFeature(Composter.class)) return;
        ResourceLocation loot = CharmLootTables.VILLAGE_FARMER;

        int max = 1;
        for (int i = 0; i < max; i++) {
            if (rand.nextFloat() < VillageDecorations.barrelsWeight) continue;

            int xx = rand.nextInt(16) + 8;
            int zz = rand.nextInt(16) + 8;

            BlockPos current = world.getHeight(pos.add(xx, 0, zz));
            boolean airAbove = world.getBlockState(current) == Blocks.AIR.getDefaultState();
            boolean grassBelow = world.getBlockState(current.offset(EnumFacing.DOWN)) == Blocks.GRASS.getDefaultState();
            if (!airAbove || !grassBelow) continue;

            if (rand.nextFloat() < 0.5f) {
                if (Charm.hasFeature(Barrel.class)) {
                    world.setBlockState(current, Barrel.barrel.getDefaultState());
                    TileEntity tile = world.getTileEntity(current);

                    if (tile instanceof TileBarrel) {
                        ((TileBarrel) tile).setLootTable(loot);
                    }
                }
            } else {
                if (Charm.hasFeature(Composter.class)) {
                    world.setBlockState(current, Composter.composter.getDefaultState());
                }
            }
        }
    }
}
