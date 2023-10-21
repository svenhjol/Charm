package svenhjol.charm.feature.pigs_find_mushrooms;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class FindMushroomGoal extends Goal {
    private final Pig pig;
    private final Level level;

    public FindMushroomGoal(Pig pig) {
        this.pig = pig;
        this.level = pig.level();
    }

    private boolean isValidBlock(BlockPos pos) {
        var state = level.getBlockState(pos);
        return state.is(PigsFindMushrooms.validBlocks);
    }

    @Override
    public boolean canUse() {
        if (pig.isBaby()) {
            return false;
        }
        if (pig.getRandom().nextInt(PigsFindMushrooms.findChance) != 0) {
            return false;
        }

        var pos = pig.blockPosition().below();
        return isValidBlock(pos);
    }

    @Override
    public void start() {
        PigsFindMushrooms.PIG_ANIMATION_TICKS.put(pig.getUUID(), adjustedTickDelay(40));
        level.broadcastEntityEvent(pig, (byte)10); // Might need change?
        pig.getNavigation().stop();
        level.playSound(null, pig.blockPosition(), PigsFindMushrooms.sniffingSound.get(), SoundSource.NEUTRAL, 1.0f, 1.0f);
    }

    @Override
    public void stop() {
        PigsFindMushrooms.PIG_ANIMATION_TICKS.remove(pig.getUUID());
    }

    @Override
    public boolean canContinueToUse() {
        return PigsFindMushrooms.PIG_ANIMATION_TICKS.getOrDefault(pig.getUUID(), 0) > 0;
    }

    @Override
    public void tick() {
        var uuid = pig.getUUID();
        var random = pig.getRandom();
        var mobGriefing = level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
        var tick = PigsFindMushrooms.PIG_ANIMATION_TICKS.getOrDefault(uuid, 0);
        pig.getNavigation().stop();

        if (tick > 4) {
            return;
        }

        var pos = pig.blockPosition().below();
        if (isValidBlock(pos)) {
            if (mobGriefing && random.nextFloat() < PigsFindMushrooms.erodeChance) {
                level.setBlock(pos, Blocks.DIRT.defaultBlockState(), 2);
                level.levelEvent(2001, pos, Block.getId(Blocks.DIRT.defaultBlockState()));
            }
            var stack = new ItemStack(random.nextBoolean() ? Items.RED_MUSHROOM : Items.BROWN_MUSHROOM, 1);
            level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d, stack));
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.NEUTRAL, 1.0f, 1.0f);

            PigsFindMushrooms.triggerUnearthedMushroom(level, pos);
        }
    }
}
