package svenhjol.charm.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;

import javax.annotation.Nullable;
import java.util.function.Consumer;

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
}
