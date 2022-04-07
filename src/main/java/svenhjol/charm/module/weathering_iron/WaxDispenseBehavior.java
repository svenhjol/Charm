package svenhjol.charm.module.weathering_iron;

import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.item.ItemStack;

public class WaxDispenseBehavior extends DefaultDispenseItemBehavior {
    @Override
    protected ItemStack execute(BlockSource blockSource, ItemStack itemStack) {
        // TODO: actually do something
        return super.execute(blockSource, itemStack);
    }
}
