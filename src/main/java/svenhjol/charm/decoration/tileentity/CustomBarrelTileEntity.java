package svenhjol.charm.decoration.tileentity;

import net.minecraft.block.BarrelBlock;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.BarrelTileEntity;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import svenhjol.charm.decoration.module.AllTheBarrels;
import svenhjol.meson.enums.IWoodType;
import svenhjol.meson.enums.VanillaWoodType;

public class CustomBarrelTileEntity extends BarrelTileEntity {
    public IWoodType wood;

    public CustomBarrelTileEntity() {
        this(VanillaWoodType.OAK);
    }

    public CustomBarrelTileEntity(IWoodType wood) {
        super(AllTheBarrels.tile);
        this.wood = wood;
        this.setCustomName(new TranslationTextComponent("block.charm.barrel_" + this.wood.getName()));
    }

    /**
     * We need to override this method to work around the strict block check.
     */
    @Override
    public void barrelTick() {
        int i = this.pos.getX();
        int j = this.pos.getY();
        int k = this.pos.getZ();
        if (this.world == null)
            return;

        this.numPlayersUsing = ChestTileEntity.calculatePlayersUsing(this.world, this, i, j, k);
        if (this.numPlayersUsing > 0) {
            this.scheduleTick();
        } else {
            BlockState blockstate = this.getBlockState();
            if (!(blockstate.getBlock() instanceof BarrelBlock)) {
                this.remove();
                return;
            }

            boolean flag = blockstate.get(BarrelBlock.PROPERTY_OPEN);
            if (flag) {
                this.playSound(blockstate, SoundEvents.BLOCK_BARREL_CLOSE);
                this.setOpenProperty(blockstate, false);
            }
        }
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("block.charm.barrel_" + this.wood.getName());
    }
}
