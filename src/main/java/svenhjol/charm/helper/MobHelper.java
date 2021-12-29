package svenhjol.charm.helper;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.levelgen.Heightmap;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static svenhjol.charm.helper.WorldHelper.isLikeAir;
import static svenhjol.charm.helper.WorldHelper.isLikeSolid;

@SuppressWarnings({"unused", "ConstantConditions"})
public class MobHelper {
    @Nullable
    public static <T extends Entity> T spawn(EntityType<T> type, ServerLevel level, BlockPos pos, MobSpawnType reason, Consumer<T> beforeAddToLevel) {
        T mob = type.create(level, null, null, null, pos, reason, false, false);
        if (mob != null) {
            beforeAddToLevel.accept(mob);
            level.addFreshEntity(mob);
        }
        return mob;
    }

    public static void setEntityAttributes(EntityType<? extends LivingEntity> entityType, AttributeSupplier.Builder attributes) {
        FabricDefaultAttributeRegistry.register(entityType, attributes);
    }

    public static boolean spawnNearBlockPos(ServerLevel level, BlockPos pos, Mob mob, BiConsumer<Mob, BlockPos> onSpawn) {
        var range = 6;
        var tries = 8;
        var random = level.getRandom();
        List<BlockPos> validPositions = new ArrayList<>();
        var surface = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos.getX(), pos.getZ());

        for (var y = surface; y < surface + range; y++) {
            for (var i = range; i > 1; --i) {
                for (var c = 1; c < tries; ++c) {
                    var checkPos = new BlockPos(pos.getX() + random.nextInt(i), y, pos.getZ() + random.nextInt(i));
                    var floor = checkPos.below();
                    var above = checkPos.above();
                    var areaIsValid = isLikeSolid(level, floor)
                        && isLikeAir(level, checkPos)
                        && isLikeAir(level, above);

                    if (areaIsValid) {
                        validPositions.add(checkPos);
                    }

                    if (validPositions.size() > 2) {
                        break;
                    }
                }
            }
        }

        if (validPositions.isEmpty()) {
            return false;
        } else {
            var spawnPos = validPositions.get(random.nextInt(validPositions.size()));
            mob.moveTo(spawnPos, 0.0F, 0.0F);
            mob.finalizeSpawn(level, level.getCurrentDifficultyAt(spawnPos), MobSpawnType.TRIGGERED, null, null);
            level.addFreshEntity(mob);
            onSpawn.accept(mob, spawnPos);
            return true;
        }
    }
}
