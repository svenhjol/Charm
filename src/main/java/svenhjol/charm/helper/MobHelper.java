package svenhjol.charm.helper;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.level.levelgen.Heightmap;
import svenhjol.charm.mixin.accessor.GoalSelectorAccessor;
import svenhjol.charm.mixin.accessor.MobAccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.BiConsumer;

import static svenhjol.charm.helper.PosHelper.isLikeAir;
import static svenhjol.charm.helper.PosHelper.isLikeSolid;

@SuppressWarnings({"unused"})
public class MobHelper {
    public static Set<WrappedGoal> getGoals(Mob mob) {
        return ((GoalSelectorAccessor)getGoalSelector(mob)).getAvailableGoals();
    }

    public static GoalSelector getGoalSelector(Mob mob) {
        return ((MobAccessor)mob).getGoalSelector();
    }

    public static <T extends Entity> T spawn(EntityType<T> type, ServerLevel world, BlockPos pos, MobSpawnType reason) {
        return type.create(world, null, null, null, pos, reason, false, false);
    }

    public static void setEntityAttributes(EntityType<? extends LivingEntity> entityType, Builder attributes) {
        FabricDefaultAttributeRegistry.register(entityType, attributes);
    }

    public static boolean spawnMobNearPos(ServerLevel world, BlockPos pos, Mob mob, BiConsumer<Mob, BlockPos> onSpawn) {
        int range = 6;
        int tries = 8;
        Random random = world.random;
        List<BlockPos> validPositions = new ArrayList<>();
        int surface = world.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos.getX(), pos.getZ());

        for (int y = surface; y < surface + range; y++) {
            for (int i = range; i > 1; --i) {
                for (int c = 1; c < tries; ++c) {
                    BlockPos checkPos = new BlockPos(pos.getX() + random.nextInt(i), y, pos.getZ() + random.nextInt(i));
                    BlockPos floor = checkPos.below();
                    BlockPos above = checkPos.above();
                    boolean areaIsValid = isLikeSolid(world, floor)
                        && isLikeAir(world, checkPos)
                        && isLikeAir(world, above);

                    if (areaIsValid)
                        validPositions.add(checkPos);

                    if (validPositions.size() > 2)
                        break;
                }
            }
        }

        if (validPositions.isEmpty()) {
            return false;
        } else {
            BlockPos spawnPos = validPositions.get(random.nextInt(validPositions.size()));
            mob.moveTo(spawnPos, 0.0F, 0.0F);
            mob.finalizeSpawn(world, world.getCurrentDifficultyAt(spawnPos), MobSpawnType.TRIGGERED, null, null);
            world.addFreshEntity(mob);
            onSpawn.accept(mob, spawnPos);
            return true;
        }
    }
}
