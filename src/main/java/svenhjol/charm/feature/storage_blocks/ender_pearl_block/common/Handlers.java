package svenhjol.charm.feature.storage_blocks.ender_pearl_block.common;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.level.Level;
import svenhjol.charm.feature.storage_blocks.ender_pearl_block.EnderPearlBlock;
import svenhjol.charm.foundation.feature.FeatureHolder;

public final class Handlers extends FeatureHolder<EnderPearlBlock> {
    public Handlers(EnderPearlBlock feature) {
        super(feature);
    }

    public void handleEntityJoin(Entity entity, Level level) {
        if (!EnderPearlBlock.convertSilverfish) return;

        // Must be a silverfish.
        if (!(entity instanceof Silverfish silverfish)) {
            return;
        }

        var goalSelector = silverfish.goalSelector;

        // Add the ender pearl block burrowing goal if it isn't already present in the silverfish AI.
        var hasGoal = goalSelector.getAvailableGoals().stream().anyMatch(
            goal -> goal.getGoal() instanceof FormEndermiteGoal);

        if (!hasGoal) {
            goalSelector.addGoal(2, new FormEndermiteGoal(silverfish));
        }
    }
}
