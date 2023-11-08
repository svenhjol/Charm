package svenhjol.charm.feature.kilns;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.feature.firing.Firing;
import svenhjol.charmony.helper.TextHelper;

public class KilnBlockEntity extends AbstractFurnaceBlockEntity {
    public KilnBlockEntity(BlockPos pos, BlockState state) {
        super(Kilns.blockEntity.get(), pos, state, Firing.recipeType.get());
    }

    @Override
    protected Component getDefaultName() {
        return TextHelper.translatable("container.charm.kiln");
    }

    @Override
    protected int getBurnDuration(ItemStack fuel) {
        return super.getBurnDuration(fuel) / 2;
    }

    @Override
    protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
        return new KilnMenu(syncId, playerInventory, this, dataAccess);
    }
}
