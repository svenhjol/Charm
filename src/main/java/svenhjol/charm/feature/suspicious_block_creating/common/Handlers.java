package svenhjol.charm.feature.suspicious_block_creating.common;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import svenhjol.charm.feature.suspicious_block_creating.SuspiciousBlockCreating;
import svenhjol.charm.foundation.feature.FeatureHolder;

public final class Handlers extends FeatureHolder<SuspiciousBlockCreating> {
    public Handlers(SuspiciousBlockCreating feature) {
        super(feature);
    }

    /**
     * Called by the piston base block mixin.
     * Ensure the block two spaces from the piston head direction is able to be converted.
     * Check for item entities in the airspace that the piston head pushes into.
     */
    public void checkAndConvert(Level level, BlockPos pos, BlockState state) {
        var direction = state.getValue(PistonBaseBlock.FACING);
        var d1 = pos.relative(direction, 1);
        var d2 = pos.relative(direction, 2);
        var target = level.getBlockState(d2);
        var random = level.getRandom();

        if (!feature().registers.blockConversions.containsKey(target.getBlock())) {
            return;
        }

        var itemEntities = level.getEntitiesOfClass(ItemEntity.class, new AABB(d1));
        if (itemEntities.isEmpty()) {
            return;
        }

        // Get one of the stacks at random
        var itemEntity = itemEntities.get(random.nextInt(itemEntities.size()));
        var stack = itemEntity.getItem();
        var result = makeSuspiciousBlock(level, d2, stack);

        if (result) {
            itemEntity.kill();

            // Do advancement for nearby players
            if (!level.isClientSide) {
                feature().advancements.createdSuspiciousBlock((ServerLevel) level, pos);
            }
        }
    }

    public boolean makeSuspiciousBlock(Level level, BlockPos pos, ItemStack stack) {
        var targetState = level.getBlockState(pos);
        var targetBlock = targetState.getBlock();

        var suspiciousBlock = feature().registers.blockConversions.getOrDefault(targetBlock, null);
        if (suspiciousBlock == null) {
            return false;
        }

        level.setBlockAndUpdate(pos, suspiciousBlock.defaultBlockState());

        var opt = level.getBlockEntity(pos, BlockEntityType.BRUSHABLE_BLOCK);
        if (opt.isPresent()) {
            var brushable = (BrushableBlockEntity) opt.get();
            brushable.lootTable = null;
            brushable.item = stack.copy();

            if (level.isClientSide) {
                var random = level.getRandom();
                for (int i = 0; i < 18; i++) {
                    level.addParticle(ParticleTypes.ASH,
                        pos.getX() + (random.nextDouble() * 1.25d),
                        pos.getY() + 1.08d,
                        pos.getZ() + (random.nextDouble() * 1.25d),
                        0.0d, 0.0d, 0.0d);
                }
            }

            level.playSound(null, pos, feature().registers.createBlockSound.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
            return true;
        }

        return false;
    }
}
