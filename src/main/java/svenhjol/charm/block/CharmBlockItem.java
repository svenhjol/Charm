package svenhjol.charm.block;

import java.util.function.BiConsumer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import svenhjol.charm.block.ICharmBlock;

public class CharmBlockItem extends BlockItem {
    private final BiConsumer<ItemStack, Boolean> inventoryTickConsumer;

    public CharmBlockItem(ICharmBlock block, Properties settings) {
        super((Block) block, settings);

        // callable inventory tick consumer from the block
        this.inventoryTickConsumer = block.getInventoryTickConsumer();
    }

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (inventoryTickConsumer != null)
            inventoryTickConsumer.accept(stack, isSelected);
    }
}
