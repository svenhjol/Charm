package svenhjol.charm.module.kilns;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.module.kilns.KilnScreenHandler;
import svenhjol.charm.module.kilns.Kilns;

public class KilnBlockEntity extends AbstractFurnaceBlockEntity {
    public KilnBlockEntity(BlockPos pos, BlockState state) {
        super(svenhjol.charm.module.kilns.Kilns.BLOCK_ENTITY, pos, state, Kilns.RECIPE_TYPE);
    }

    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent("container.charm.kiln");
    }

    @Override
    protected int getBurnDuration(ItemStack fuel) {
        return super.getBurnDuration(fuel) / 2;
    }

    @Override
    protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
        return new KilnScreenHandler(syncId, playerInventory, this, this.dataAccess);
    }
}
