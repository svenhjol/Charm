package svenhjol.charm.blockentity;

import net.minecraft.block.BarrelBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.sound.SoundEvents;
import svenhjol.charm.mixin.accessor.BarrelBlockEntityAccessor;
import svenhjol.charm.module.VariantBarrels;

public class VariantBarrelBlockEntity extends BarrelBlockEntity {
    public VariantBarrelBlockEntity() {
        BarrelBlockEntityAccessor.invokeConstructor(VariantBarrels.TILE);
    }

    /**
     * We need to override this method to work around the strict block check.
     */
    @Override
    public void tick() {
        int i = this.pos.getX();
        int j = this.pos.getY();
        int k = this.pos.getZ();
        if (this.world == null)
            return;

        ((BarrelBlockEntityAccessor)this).setViewerCount(ChestBlockEntity.countViewers(this.world, this, i, j, k));
        if (((BarrelBlockEntityAccessor)this).getViewerCount() > 0) {
            ((BarrelBlockEntityAccessor)this).callScheduleUpdate();
        } else {
            BlockState blockstate = this.getCachedState();
            if (!(blockstate.getBlock() instanceof BarrelBlock)) {
                this.markRemoved();
                return;
            }

            boolean flag = blockstate.get(BarrelBlock.OPEN);
            if (flag) {
                ((BarrelBlockEntityAccessor)this).callPlaySound(blockstate, SoundEvents.BLOCK_BARREL_CLOSE);
                ((BarrelBlockEntityAccessor)this).callSetOpen(blockstate, false);
            }
        }
    }
}
