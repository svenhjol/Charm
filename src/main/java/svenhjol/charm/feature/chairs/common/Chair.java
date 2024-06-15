package svenhjol.charm.feature.chairs.common;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.StairBlock;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.feature.chairs.Chairs;

public class Chair extends Entity {
    private static final Chairs CHAIRS = Resolve.feature(Chairs.class);
    public Chair(EntityType<Chair> entityType, Level level) {
        super(entityType, level);
    }

    public Chair(Level level, BlockPos pos) {
        super(CHAIRS.registers.entity.get(), level);
        setPos(pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d);

        var facing = level.getBlockState(pos)
            .getValue(StairBlock.FACING)
            .getOpposite();

        setYRot(facing.toYRot());
    }

    @Override
    public void tick() {
        super.tick();

        var level = level();
        var pos = blockPosition();
        var state = level.getBlockState(pos);
        var block = state.getBlock();

        if (!level.isClientSide()) {
            var stateAbove = level.getBlockState(pos.above());

            if (!(block instanceof StairBlock)) {
                unRide();
                remove(RemovalReason.DISCARDED);
                CHAIRS.log().debug("Removing because no longer a stairs block");
            } else if (stateAbove.isCollisionShapeFullBlock(level, pos.above())) {
                unRide();
                remove(RemovalReason.DISCARDED);
                CHAIRS.log().debug("Removing because block above is invalid");
            } else if (!hasExactlyOnePlayerPassenger()) {
                remove(RemovalReason.DISCARDED);
                CHAIRS.log().debug("Removing because no passengers");
            }
        }
    }

    @Override
    public double getPassengersRidingOffset() {
        return -0.25F;
    }

    @Override
    protected void positionRider(Entity entity, MoveFunction moveFunction) {
        super.positionRider(entity, moveFunction);
        clampRotation(entity);
    }

    @Override
    public void onPassengerTurned(Entity entity) {
        clampRotation(entity);
    }

    private void clampRotation(Entity entity) {
        entity.setYBodyRot(this.getYRot());
        var f = Mth.wrapDegrees(entity.getYRot() - this.getYRot());
        var g = Mth.clamp(f, -105.0f, 105.0f);
        entity.yRotO += g - f;
        entity.setYRot(entity.getYRot() + g - f);
        entity.setYHeadRot(entity.getYRot());
    }

    @Override
    protected void defineSynchedData() {
        // no op
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        // no op
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        // no op
    }
}
