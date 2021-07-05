package svenhjol.charm.module.potion_of_hogsbane;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.event.PlayerTickCallback;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.mixin.accessor.HoglinAiAccessor;
import svenhjol.charm.potion.CharmPotion;

import java.util.List;

@CommonModule(mod = Charm.MOD_ID, description = "Causes all hoglins in the vicinity to run away from you.")
public class PotionOfHogsbane extends CharmModule {
    public static HogsbaneEffect HOGSBANE_EFFECT;
    public static CharmPotion HOGSPANE_POTION;
    public static CharmPotion LONG_HOGSBANE_POTION;

    public static final ResourceLocation TRIGGER_SCARED_HOGLINS = new ResourceLocation(Charm.MOD_ID, "scared_hoglins");

    @Override
    public void register() {
        HOGSBANE_EFFECT = new HogsbaneEffect(this);
        HOGSPANE_POTION = new HogsbanePotion(this);
        LONG_HOGSBANE_POTION = new LongHogsbanePotion(this);
    }

    @Override
    public void runWhenEnabled() {
        PlayerTickCallback.EVENT.register(this::handlePlayerTick);
        AttackEntityCallback.EVENT.register(this::handleAttackEntity);
    }

    private void handlePlayerTick(Player player) {
        if (!player.level.isClientSide && player.hasEffect(HOGSBANE_EFFECT)) {
            boolean tick = player.level.getGameTime() % 30 == 0;

            if (tick) {
                ServerLevel world = (ServerLevel)player.level;
                BlockPos pos = player.blockPosition();

                List<Hoglin> hoglins = world.getEntitiesOfClass(Hoglin.class, new AABB(pos).inflate(12.0D));
                hoglins.forEach(hoglin -> {
                    hoglin.setAggressive(false);
                    hoglin.getBrain().setMemory(MemoryModuleType.NEAREST_REPELLENT, player.blockPosition());
                    hoglin.getBrain().setMemory(MemoryModuleType.AVOID_TARGET, player);
                });

                if (hoglins.size() >= 1)
                    PotionOfHogsbane.triggerScaredHoglins((ServerPlayer) player);
            }
        }
    }

    private InteractionResult handleAttackEntity(Player player, Level world, InteractionHand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        if (entity instanceof Hoglin hoglin && player.hasEffect(HOGSBANE_EFFECT)) {
            hoglin.hurt(DamageSource.GENERIC, 4);
            HoglinAiAccessor.invokeSetAvoidTarget(hoglin, player);
        }
        return InteractionResult.PASS;
    }

    public static boolean hasHogsbaneEffect(LivingEntity entity) {
        return entity.hasEffect(HOGSBANE_EFFECT);
    }

    public static void triggerScaredHoglins(ServerPlayer player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_SCARED_HOGLINS);
    }
}
