package svenhjol.charm.module.chairs;


import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.StairBlock;
import svenhjol.charm.helper.LogHelper;

public class ChairEntity extends Entity {
    public ChairEntity(EntityType<? extends ChairEntity> type, Level level) {
        super(type, level);
    }

    public ChairEntity(Level level, BlockPos pos) {
        super(Chairs.CHAIR, level);
        setPos(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);

        var facing = level.getBlockState(pos).getValue(StairBlock.FACING).getOpposite();
        setYRot(facing.toYRot());
    }

    @Override
    protected void defineSynchedData() {
        // no
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        // nope
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        // nah
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    @Override
    public double getPassengersRidingOffset() {
        return -0.25F;
    }

    @Override
    public void tick() {
        super.tick();
        var pos = blockPosition();
        var state = level.getBlockState(pos);
        var block = state.getBlock();

        if (!level.isClientSide) {
            var stateAbove = level.getBlockState(pos.above());

            if (!(block instanceof StairBlock)) {

                unRide();
                remove(RemovalReason.DISCARDED);
                LogHelper.debug(getClass(), "Removing because no longer a stairs block");

            } else if (stateAbove.isCollisionShapeFullBlock(level, pos.above())) {

                unRide();
                remove(RemovalReason.DISCARDED);
                LogHelper.debug(getClass(), "Removing because block above is invalid");

            } else if (!hasExactlyOnePlayerPassenger()) {

                remove(RemovalReason.DISCARDED);
                LogHelper.debug(getClass(), "Removing because no passengers");

            }
        }
    }

    @Override
    public void positionRider(Entity entity) {
        if (!hasPassenger(entity)) return;
        super.positionRider(entity);
        clampRotation(entity);
    }

    @Override
    public void onPassengerTurned(Entity entity) {
        clampRotation(entity);
    }

    private void clampRotation(Entity entity) {
        entity.setYBodyRot(this.getYRot());
        float f = Mth.wrapDegrees(entity.getYRot() - this.getYRot());
        float g = Mth.clamp(f, -105.0f, 105.0f);
        entity.yRotO += g - f;
        entity.setYRot(entity.getYRot() + g - f);
        entity.setYHeadRot(entity.getYRot());
    }
}
