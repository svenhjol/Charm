package svenhjol.charm.charmony.common.dispenser;

import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.charmony.common.CommonRegistry;

import java.util.List;

public class CompositeDispenseItemBehavior extends DefaultDispenseItemBehavior {
    @Override
    protected ItemStack execute(BlockSource blockSource, ItemStack stack) {
        var behaviors = CommonRegistry.conditionalDispenserBehaviors()
            .getOrDefault(stack.getItem(), List.of());

        for (var behavior : behaviors) {
            var result = behavior.accept(this, blockSource, stack);
            if (result) {
                return behavior.stack().orElse(stack);
            }
        }
        
        return super.execute(blockSource, stack);
    }
}
