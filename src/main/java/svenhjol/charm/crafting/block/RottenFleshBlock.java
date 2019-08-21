package svenhjol.charm.crafting.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemGroup;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import svenhjol.charm.Charm;
import svenhjol.charm.crafting.module.BlockOfRottenFlesh;
import svenhjol.meson.MesonBlock;
import svenhjol.meson.helper.WorldHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RottenFleshBlock extends MesonBlock
{
    private static List<Block> transformables = new ArrayList<>(Arrays.asList(
        Blocks.DIRT, Blocks.GRASS, Blocks.GRASS_PATH));

    public RottenFleshBlock()
    {
        super(Block.Properties
            .create(Material.ORGANIC)
            .sound(SoundType.GROUND)
            .hardnessAndResistance(BlockOfRottenFlesh.hardness, BlockOfRottenFlesh.resistance)
            .harvestTool(ToolType.SHOVEL)
            .tickRandomly()
        );

        register(new ResourceLocation(Charm.MOD_ID, "rotten_flesh_block"));
    }

    @Override
    public ItemGroup getItemGroup()
    {
        return ItemGroup.BUILDING_BLOCKS;
    }

    @Override
    public void tick(BlockState state, World worldIn, BlockPos pos, Random random)
    {
        super.tick(state, worldIn, pos, random);

        if (!worldIn.isRemote) {
            if (!worldIn.isAreaLoaded(pos, 2)) return;;

            // transform soil to something else
            ArrayList<Block> transforms = new ArrayList<>(Arrays.asList(Blocks.PODZOL, Blocks.MYCELIUM));
            transforms.forEach(blockType -> {
                if (worldIn.getBlockState(pos).getBlock() != blockType && WorldHelper.getBiomeAtPos(worldIn, pos).getSurfaceBuilder().config.getTop().getBlock() == blockType) {
                    if (transformables.contains(worldIn.getBlockState(pos.up()).getBlock())) {
                        worldIn.setBlockState(pos.up(), blockType.getDefaultState(), 2);
                    }
                }
            });

            // transform self to dirt if next to water
            for (Direction facing : Direction.values()) {
                if (worldIn.getBlockState(pos.offset(facing)).getBlock() == Blocks.WATER) {
                    worldIn.setBlockState(pos, Blocks.DIRT.getDefaultState(), 2);
                }
            }
        }
    }

    // copypasta from MyceliumBlock
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, World world, BlockPos pos, Random rand)
    {
        super.animateTick(state, world, pos, rand);
        if (rand.nextInt(10) == 0) {
            world.addParticle(ParticleTypes.MYCELIUM, (float)pos.getX() + rand.nextFloat(), (float)pos.getY() + 1.1F, (float)pos.getZ() + rand.nextFloat(), 0.0D, 0.0D, 0.0D);
        }
    }
}
