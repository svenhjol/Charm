package svenhjol.charm.foundation.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;

import javax.annotation.Nullable;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public final class MobHelper {
    /**
     * Helper method to spawn a mob at a specific block position.
     * @param type Mob type.
     * @param level World.
     * @param pos The position to spawn at.
     * @param reason See MobSpawnType enum. Probably use NATURAL.
     * @param beforeAddToLevel An optional consumer to run before adding the mob to the world.
     * @return New mob instance.
     * @param <T> Mob type.
     */
    @SuppressWarnings("UnusedReturnValue")
    @Nullable
    public static <T extends Mob> T spawn(EntityType<T> type, ServerLevel level, BlockPos pos, MobSpawnType reason, Consumer<T> beforeAddToLevel) {
        T mob = type.create(level);
        if (mob != null) {
            beforeAddToLevel.accept(mob);
            level.addFreshEntity(mob);
            mob.setPos(pos.getX() + 0.5d, pos.getY() + 1.0d, pos.getZ() + 0.5d);
        }

        return mob;
    }
}
