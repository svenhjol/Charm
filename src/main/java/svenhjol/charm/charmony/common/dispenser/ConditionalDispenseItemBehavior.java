package svenhjol.charm.charmony.common.dispenser;

import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public interface ConditionalDispenseItemBehavior {
    boolean accept(CompositeDispenseItemBehavior behavior, BlockSource blockSource, ItemStack stack);
    
    Optional<ItemStack> stack();
}
