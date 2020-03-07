package svenhjol.charm.world.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.ItemGroup;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import svenhjol.charm.base.CharmSounds;
import svenhjol.charm.world.module.Fumaroles;
import svenhjol.meson.MesonModule;
import svenhjol.meson.block.MesonBlock;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

@SuppressWarnings("deprecation")
public class FumaroleBlock extends MesonBlock {
    public FumaroleBlock(MesonModule module) {
        super(module, "fumarole", Block.Properties
            .create(Material.ROCK, MaterialColor.NETHERRACK)
            .hardnessAndResistance(0.4F)
            .tickRandomly()
        );
    }

    @Override
    public ItemGroup getItemGroup() {
        return ItemGroup.BUILDING_BLOCKS;
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (tryAbsorb(worldIn, pos)) {
            erupt(worldIn, pos);
        }
    }

    protected boolean tryAbsorb(World world, BlockPos pos) {
        boolean absorbed = false;

        for (Direction direction : Direction.values()) {
            BlockPos next = pos.offset(direction);
            BlockState state = world.getBlockState(next);
            IFluidState fluid = world.getFluidState(next);

            if (fluid.isTagged(FluidTags.LAVA)) {
                if (state.getBlock() instanceof IBucketPickupHandler
                    && ((IBucketPickupHandler) state.getBlock()).pickupFluid(world, next, state) != Fluids.EMPTY
                ) {
                    absorbed = true;
                } else if (state.getBlock() == Blocks.LAVA) {
                    absorbed = true;
                }
            }
        }

        return absorbed;
    }

    protected void erupt(World world, BlockPos pos) {
        if (!world.isAirBlock(pos.up()) || world.isAirBlock(pos.down())) return;

        Random rand = world.rand;
        if (rand.nextFloat() < 0.5F) return;

        if (!world.isRemote) {
            ServerWorld serverWorld = (ServerWorld) world;
            float spread = 0.05F;
            double px = pos.getX() + 0.5D + (rand.nextFloat() - 0.5D) * spread;
            double py = pos.getY() + 0.5D + (rand.nextFloat() - 0.5D) * spread;
            double pz = pos.getZ() + 0.5D + (rand.nextFloat() - 0.5D) * spread;
            serverWorld.spawnParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, px, py, pz, 60, -0.2D, 0D, 0D, 0.11D);
            serverWorld.spawnParticle(ParticleTypes.LARGE_SMOKE, px, py, pz, 60, -0.2D, 0D, 0D, 0.04D);
            serverWorld.playSound(null, pos, CharmSounds.FUMAROLE_ERUPT, SoundCategory.BLOCKS, (float) Fumaroles.eruptionVolume, 0.85F + (world.rand.nextFloat()) * 0.25F);
        }

        AxisAlignedBB bb = new AxisAlignedBB(pos);
        Predicate<Entity> selector = entity -> true;
        List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, bb.grow(0, 10, 0), selector);

        for (Entity entity : entities) {
            Vec3d motion = entity.getMotion();
            double speed = 1.75D / ((entity.getPosition().getY() - pos.getY()) * 0.75D);
            entity.setMotion(motion.x, speed, motion.z);
            entity.velocityChanged = true;
        }
    }

    @Override
    public void tick(BlockState state, World world, BlockPos pos, Random rand) {
        super.tick(state, world, pos, rand);
        erupt(world, pos);
    }

    @Override
    public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
        super.animateTick(state, world, pos, rand);

        if (rand.nextFloat() < 0.1F) {
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), CharmSounds.FUMAROLE_BUBBLING, SoundCategory.BLOCKS, 0.13F, 0.9F + (world.rand.nextFloat()) * 0.2F, false);
        }

        if (!world.isAirBlock(pos.up())) return;

        if (rand.nextInt(3) == 0) {
            float spread = 0.5F;
            int amount = rand.nextInt(4) + 1;

            for (int i = 0; i < amount; i++) {
                float speed = 0.05F + rand.nextFloat() * 0.1F;
                double px = pos.getX() + 0.5D + (rand.nextFloat() - 0.5D) * spread;
                double py = pos.getY() + 1.0D + (rand.nextFloat() - 0.5D) * spread;
                double pz = pos.getZ() + 0.5D + (rand.nextFloat() - 0.5D) * spread;

                BasicParticleType smoke = rand.nextFloat() < 0.75F ? ParticleTypes.CAMPFIRE_COSY_SMOKE : ParticleTypes.LARGE_SMOKE;
                world.addParticle(smoke, px, py, pz, 0.0F, speed, 0.0F);
            }

            world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 0.13F, 0.75F + (rand.nextFloat() * 0.25F), false);
        }
    }
}
