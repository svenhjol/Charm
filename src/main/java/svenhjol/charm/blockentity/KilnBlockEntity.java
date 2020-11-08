package svenhjol.charm.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import svenhjol.charm.module.Kilns;
import svenhjol.charm.screenhandler.KilnScreenHandler;

public class KilnBlockEntity extends AbstractFurnaceBlockEntity {
    public KilnBlockEntity(BlockPos pos, BlockState state) {
        super(Kilns.BLOCK_ENTITY, pos, state, Kilns.RECIPE_TYPE);
    }

    @Override
    protected Text getContainerName() {
        return new TranslatableText("container.charm.kiln");
    }

    @Override
    protected int getFuelTime(ItemStack fuel) {
        return super.getFuelTime(fuel) / 2;
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new KilnScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }
}
