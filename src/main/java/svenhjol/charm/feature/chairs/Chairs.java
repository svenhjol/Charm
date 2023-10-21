package svenhjol.charm.feature.chairs;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.phys.BlockHitResult;
import svenhjol.charm.Charm;
import svenhjol.charmony.common.CommonFeature;
import svenhjol.charmony.feature.advancements.Advancements;
import svenhjol.charmony_api.event.BlockUseEvent;

import java.util.function.Supplier;

/**
 * Inspired by Quark's SitInStairs module.
 */
public class Chairs extends CommonFeature {
    static Supplier<EntityType<ChairEntity>> entity;

    @Override
    public String description() {
        return "Right-click (with empty hand) on any stairs block to sit down.";
    }

    @Override
    public void register() {
        entity = mod().registry().entity("chair", () -> EntityType.Builder
            .<ChairEntity>of(ChairEntity::new, MobCategory.MISC)
            .sized(0.25F, 0.25F)
            .clientTrackingRange(1)
            .updateInterval(1));
    }

    @Override
    public void runWhenEnabled() {
        BlockUseEvent.INSTANCE.handle(this::handleBlockUse);
    }

    private InteractionResult handleBlockUse(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        var log = mod().log();

        if (!level.isClientSide()
            && player.getMainHandItem().isEmpty()
            && !player.isPassenger()
            && !player.isCrouching()
        ) {
            var pos = hitResult.getBlockPos();
            var state = level.getBlockState(pos);
            var stateAbove = level.getBlockState(pos.above());
            var block = state.getBlock();

            if (block instanceof StairBlock
                && state.getValue(StairBlock.HALF) == Half.BOTTOM
                && !stateAbove.isCollisionShapeFullBlock(level, pos.above())
            ) {
                var chair = new ChairEntity(level, pos);
                level.addFreshEntity(chair);
                log.debug(getClass(), "Added new chair entity");

                var result = player.startRiding(chair);
                log.debug(getClass(), "Player is now riding");
                if (result) {
                    player.moveTo(chair.getX(), chair.getY(), chair.getZ());
                    player.setPos(chair.getX(), chair.getY(), chair.getZ());
                    log.debug(getClass(), "Moved player to chair pos");
                }

                triggerSatOnChair(player);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    public static void triggerSatOnChair(Player player) {
        Advancements.trigger(new ResourceLocation(Charm.ID, "sat_on_chair"), player);
    }
}
