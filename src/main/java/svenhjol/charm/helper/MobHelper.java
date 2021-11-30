package svenhjol.charm.helper;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

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

    public static void setEntityAttributes(EntityType<? extends LivingEntity> entityType, AttributeSupplier.Builder attributes) {
        FabricDefaultAttributeRegistry.register(entityType, attributes);
    }
}
