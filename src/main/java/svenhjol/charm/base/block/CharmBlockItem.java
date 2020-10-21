package svenhjol.charm.base.block;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.function.BiConsumer;

public class CharmBlockItem extends BlockItem {
    private final BiConsumer<ItemStack, Boolean> inventoryTickConsumer;

    public CharmBlockItem(ICharmBlock block, Settings settings) {
        super((Block) block, settings);

        // callable inventory tick consumer from the block
        this.inventoryTickConsumer = block.getInventoryTickConsumer();
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (inventoryTickConsumer != null)
            inventoryTickConsumer.accept(stack, isSelected);
    }
}
