package svenhjol.charm.feature.chairs.common;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import svenhjol.charm.charmony.feature.FeatureHolder;
import svenhjol.charm.feature.chairs.Chairs;

public final class Handlers extends FeatureHolder<Chairs> {
    public Handlers(Chairs feature) {
        super(feature);
    }

    public InteractionResult blockUse(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        var pos = hitResult.getBlockPos();

        if (!level.isClientSide()
            && player.getMainHandItem().isEmpty()
            && !player.isPassenger()
            && !player.isCrouching()
            && isReachableChair(player, pos)
        ) {
            var state = level.getBlockState(pos);
            var stateAbove = level.getBlockState(pos.above());
            var block = state.getBlock();
            var aabb = AABB.of(new BoundingBox(pos));
            var existingChairs = level.getEntities(null, aabb).stream().filter(e -> e instanceof Chair).toList();

            if (block instanceof StairBlock
                && state.getValue(StairBlock.HALF) == Half.BOTTOM
                && (stateAbove.isAir() || stateAbove.getFluidState().is(FluidTags.WATER))
                && existingChairs.isEmpty()
            ) {
                var chair = new Chair(level, pos);
                level.addFreshEntity(chair);
                log().dev("Added new chair entity");

                var result = player.startRiding(chair);
                log().dev("Player is now riding");
                if (result) {
                    player.moveTo(chair.getX(), chair.getY(), chair.getZ());
                    player.setPos(chair.getX(), chair.getY(), chair.getZ());
                    log().dev("Moved player to chair pos");
                }

                feature().advancements.satOnChair(player);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    private boolean isReachableChair(Player player, BlockPos chair) {
        Vec3 vec3 = Vec3.atBottomCenterOf(chair);
        return Math.abs(player.getX() - vec3.x()) <= 3.0 && Math.abs(player.getY() - vec3.y()) <= 2.0 && Math.abs(player.getZ() - vec3.z()) <= 3.0;
    }
}
