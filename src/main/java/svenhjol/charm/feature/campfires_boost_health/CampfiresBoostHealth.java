package svenhjol.charm.feature.campfires_boost_health;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import svenhjol.charm.Charm;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmonyFeature;
import svenhjol.charmony.feature.advancements.Advancements;

@Feature(mod = Charm.MOD_ID, description = "Standing within range and sight of a lit campfire provides a small regeneration boost.\n" +
    "It does not work if there are enemies nearby.")
public class CampfiresBoostHealth extends CharmonyFeature {
    public static final int INTERVAL_CHECK = 200;
    public static final int LENGTH_OF_REGENERATION = 2;
    public static final int DISTANCE_TO_FIRE = 6;

    public static void tryRegeneratePlayersAroundFire(Level level, BlockPos pos) {
        if (level.getGameTime() % CampfiresBoostHealth.INTERVAL_CHECK == 0) {
            var bb = (new AABB(pos)).inflate(DISTANCE_TO_FIRE);
            var players = level.getEntitiesOfClass(Player.class, bb);
            var hostiles = level.getEntitiesOfClass(Monster.class, bb);
            if (!hostiles.isEmpty()) return;

            players.forEach(player -> {
                if (playerHasLineOfSight(player, pos) || playerHasLineOfSight(player, pos.above())) {
                    player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, CampfiresBoostHealth.LENGTH_OF_REGENERATION * 20, 1));
                    triggerHealNearCampfire(player);
                }
            });
        }
    }

    private static boolean playerHasLineOfSight(Player player, BlockPos target) {
        var playerVec = new Vec3(player.getX(), player.getEyeY(), player.getZ());
        var targetVec = new Vec3(target.getX(), target.getY(), target.getZ());
        var collision = player.level().clip(new ClipContext(playerVec, targetVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player)).getType();
        return collision == HitResult.Type.MISS;
    }

    public static void triggerHealNearCampfire(Player player) {
        Advancements.trigger(Charm.instance().makeId("healed_near_campfire"), player);
    }
}
