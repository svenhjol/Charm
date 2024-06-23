package svenhjol.charm.feature.cooking_pots.common.dispenser;

import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import svenhjol.charm.charmony.common.dispenser.CompositeDispenseItemBehavior;
import svenhjol.charm.charmony.common.dispenser.ConditionalDispenseItemBehavior;
import svenhjol.charm.charmony.feature.FeatureResolver;
import svenhjol.charm.feature.cooking_pots.CookingPots;
import svenhjol.charm.feature.cooking_pots.common.CookingPotBlockEntity;

import java.util.Optional;

public class WaterBucketBehavior implements FeatureResolver<CookingPots>, ConditionalDispenseItemBehavior {
    private ItemStack stack;
    
    @Override
    public boolean accept(CompositeDispenseItemBehavior behavior, BlockSource blockSource, ItemStack stack) {
        var serverLevel = blockSource.level();
        var dispenserState = blockSource.state();
        var pos = blockSource.pos().relative(dispenserState.getValue(DispenserBlock.FACING));

        if (serverLevel.getBlockEntity(pos) instanceof CookingPotBlockEntity cask) {
            var result = feature().handlers.dispenserAddToPot(cask, stack);
            if (result) {
                stack.shrink(1);
                this.stack = stack;
                return true;
            }
        }
        
        return false;
    }

    @Override
    public Optional<ItemStack> stack() {
        return Optional.ofNullable(stack);
    }

    @Override
    public Class<CookingPots> typeForFeature() {
        return CookingPots.class;
    }
}
