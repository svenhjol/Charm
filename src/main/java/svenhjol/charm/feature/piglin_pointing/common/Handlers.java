package svenhjol.charm.feature.piglin_pointing.common;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.feature.piglin_pointing.PiglinPointing;
import svenhjol.charm.foundation.feature.FeatureHolder;

public final class Handlers extends FeatureHolder<PiglinPointing> {
    public Handlers(PiglinPointing feature) {
        super(feature);
    }

    /**
     * Called by mixin when piglin has finished considering the block.
     * @see svenhjol.charm.mixin.feature.piglin_pointing.PiglinAiMixin
     */
    public void checkBlockAndFindStructure(Piglin piglin, ItemStack stack) {
        if (piglin.level() instanceof ServerLevel level) {
            BlockPos source = piglin.blockPosition();
            BlockPos target = null;

            for (var pair : feature().registers.DIRECTION_BARTERING) {
                if (stack.is(pair.getFirst())) {
                    target = level.findNearestMapStructure(pair.getSecond(), source, 100, false);
                }
            }

            if (target != null) {
                piglin.getBrain().setMemoryWithExpiry(feature().registers.pointingAtTarget.get(), target, 100L);
                feature().advancements.piglinProvidedDirections(level, source);
            }
        }
    }

    /**
     * Called by mixin to update the piglin entity and model as part of a ticking update check.
     * @see svenhjol.charm.mixin.feature.piglin_pointing.PiglinAiMixin
     */
    public void setPointing(Piglin piglin) {
        piglin.getBrain().getMemory(feature().registers.pointingAtTarget.get()).ifPresentOrElse(
            pos -> {
                piglin.getLookControl().setLookAt(pos.getX(), 60, pos.getZ());
                piglin.getNavigation().stop();
                piglin.getEntityData().set(feature().registers.entityDataIsPointing, true);
            },
            () -> {
                piglin.getEntityData().set(feature().registers.entityDataIsPointing, false);
            }
        );
    }

    /**
     * Called by mixin to check whether a nearby item should be picked up.
     * @see svenhjol.charm.mixin.feature.piglin_pointing.PiglinAiMixin
     */
    public boolean wantsToPickup(Piglin piglin, ItemStack stack) {
        return !piglin.isBaby()
            && isBarteringItem(stack)
            && isNotAdmiringOrPointing(piglin);
    }

    /**
     * Called by mixin to actually pick up the item and what do to with it.
     * @see svenhjol.charm.mixin.feature.piglin_pointing.PiglinAiMixin
     */
    public boolean tryToPickup(Piglin piglin, ItemStack stack) {
        if (isBarteringItem(stack)) {
            piglin.getBrain().eraseMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM);
            piglin.getBrain().setMemoryWithExpiry(MemoryModuleType.ADMIRING_ITEM, true, 40L);
            PiglinAi.holdInOffhand(piglin, stack);
            return true;
        }
        return false;
    }

    public boolean isNotAdmiringOrPointing(Piglin piglin) {
        return piglin.getBrain().getMemory(MemoryModuleType.ADMIRING_ITEM).isEmpty()
            && piglin.getBrain().getMemory(feature().registers.pointingAtTarget.get()).isEmpty();
    }

    public boolean isPointing(Piglin piglin) {
        return piglin.getEntityData().get(feature().registers.entityDataIsPointing);
    }

    public boolean isBarteringItem(ItemStack stack) {
        return stack.is(Tags.PIGLIN_BARTERS_FOR_DIRECTIONS);
    }
}
