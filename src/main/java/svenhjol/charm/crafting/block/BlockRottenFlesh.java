package svenhjol.charm.crafting.block;

import net.minecraft.block.BlockDirt;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import svenhjol.charm.Charm;
import svenhjol.charm.crafting.feature.RottenFleshBlock;
import svenhjol.meson.MesonBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockRottenFlesh extends MesonBlock
{
    public static List<IBlockState> transformables  = new ArrayList<IBlockState>()
    {{
        add(Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
        add(Blocks.GRASS.getDefaultState());
    }};

    public BlockRottenFlesh()
    {
        super(Material.SPONGE, "rotten_flesh_block");

        setHardness(RottenFleshBlock.hardness);
        setResistance(RottenFleshBlock.resistance);
        setHarvestLevel("sword", RottenFleshBlock.harvestLevel);
        setSoundType(SoundType.SLIME);
        setTickRandomly(true);
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
    {
        super.updateTick(world, pos, state, rand);

        if (!world.isRemote) {
            if (!RottenFleshBlock.transformToPodzol && !RottenFleshBlock.transformToSoil) return;
            if (!world.isAreaLoaded(pos, 2)) return; // Forge: prevent loading unloaded chunks

            // transform sides to podzol if dirt above
            if (RottenFleshBlock.transformToPodzol) {
                List<BlockPos> sides = new ArrayList<BlockPos>()
                {{
                    add(pos.up());
                }};

                IBlockState podzol = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.PODZOL);

                for (BlockPos side : sides) {
                    if (world.getBlockState(pos) == podzol) break;
                    if (transformables.contains(world.getBlockState(side))) {
                        world.setBlockState(side, podzol, 2);
                    }
                }
            }

            // transform self to dirt if next to water
            if (RottenFleshBlock.transformToSoil) {
                List<BlockPos> sides = new ArrayList<BlockPos>()
                {{
                    add(pos.north());
                    add(pos.east());
                    add(pos.south());
                    add(pos.west());
                    add(pos.up());
                    add(pos.down());
                }};

                for (BlockPos side : sides) {
                    if (world.getBlockState(side).getBlock() == Blocks.WATER) {
                        IBlockState transformTo = Blocks.DIRT.getDefaultState();
                        world.setBlockState(pos, transformTo, 2);
                    }
                }
            }
        }
    }

    // copypasta from BlockMycelium
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        super.randomDisplayTick(stateIn, worldIn, pos, rand);

        if (RottenFleshBlock.showParticles && rand.nextInt(10) == 0) {
            worldIn.spawnParticle(EnumParticleTypes.TOWN_AURA, (double)((float)pos.getX() + rand.nextFloat()), (double)((float)pos.getY() + 1.1F), (double)((float)pos.getZ() + rand.nextFloat()), 0.0D, 0.0D, 0.0D);
        }
    }
}
