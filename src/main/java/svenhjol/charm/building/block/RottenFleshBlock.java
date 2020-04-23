package svenhjol.charm.building.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemGroup;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.ToolType;
import svenhjol.charm.building.module.BlockOfRottenFlesh;
import svenhjol.meson.MesonModule;
import svenhjol.meson.block.MesonBlock;

import java.util.Random;

public class RottenFleshBlock extends MesonBlock {
    public RottenFleshBlock(final MesonModule module) {
        super(module, "rotten_flesh_block", Block.Properties
                .create(Material.ORGANIC)
                .sound(SoundType.GROUND)
                .hardnessAndResistance(1.0F, 2.0F)
                .harvestTool(ToolType.SHOVEL)
                .tickRandomly()
        );
    }

    @Override
    public ItemGroup getItemGroup() {
        return ItemGroup.BUILDING_BLOCKS;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void tick(final BlockState state, final ServerWorld worldIn, final BlockPos pos, final Random random) {
        super.tick(state, worldIn, pos, random);

        if (!worldIn.isRemote) {
            if (!worldIn.isAreaLoaded(pos, 2)) return;

            // transform self to dirt if next to water
            for (final Direction facing : Direction.values()) {
                if (worldIn.getBlockState(pos.offset(facing)).getBlock() == Blocks.WATER) {
                    worldIn.setBlockState(pos, Blocks.DIRT.getDefaultState(), 2);
                }
            }

            final BlockState aboveState = worldIn.getBlockState(pos.up(1));
            final BlockState plantState = worldIn.getBlockState(pos.up(2));
            if (plantState.getBlock() instanceof IPlantable && aboveState.canSustainPlant(worldIn, pos.up(1), Direction.UP, (IPlantable) plantState.getBlock())) {
                if (plantState.getBlock() instanceof IGrowable) {
                    if (random.nextInt(10) <= BlockOfRottenFlesh.growChance) {
                        final IGrowable growable = (IGrowable) plantState.getBlock();
                        if (growable.canGrow(worldIn, pos.up(2), plantState, worldIn.isRemote)) {
                            growable.grow(worldIn, worldIn.rand, pos.up(2), plantState);
                            worldIn.playEvent(2005, pos.up(2), 0);
                        }
                    }
                }
            }
        }
    }

    // copypasta from MyceliumBlock
    @OnlyIn(Dist.CLIENT)
    public void animateTick(final BlockState state, final World world, final BlockPos pos, final Random rand) {
        super.animateTick(state, world, pos, rand);
        if (rand.nextInt(10) == 0) {
            world.addParticle(ParticleTypes.MYCELIUM, (float) pos.getX() + rand.nextFloat(), (float) pos.getY() + 1.1F, (float) pos.getZ() + rand.nextFloat(), 0.0D, 0.0D, 0.0D);
        }
    }
}
