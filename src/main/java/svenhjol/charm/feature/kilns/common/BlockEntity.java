package svenhjol.charm.feature.kilns.common;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.feature.firing.Firing;
import svenhjol.charm.feature.kilns.Kilns;
import svenhjol.charm.foundation.Resolve;

public class BlockEntity extends AbstractFurnaceBlockEntity {
    private static final Kilns KILNS = Resolve.feature(Kilns.class);
    private static final Firing FIRING = Resolve.feature(Firing.class);

    public BlockEntity(BlockPos pos, BlockState state) {
        super(KILNS.registers.blockEntity.get(), pos, state, FIRING.registers.recipeType.get());
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.charm.kiln");
    }

    @Override
    protected int getBurnDuration(ItemStack fuel) {
        return super.getBurnDuration(fuel) / 2;
    }

    @Override
    protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
        return new Menu(syncId, playerInventory, this, dataAccess);
    }
}
