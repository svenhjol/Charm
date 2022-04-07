package svenhjol.charm.module.weathering_iron;

import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;

public class WaxDispenseBehavior extends OptionalDispenseItemBehavior {
    @Override
    protected ItemStack execute(BlockSource source, ItemStack stack) {
        var pos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
        var level = source.getLevel();
        var state = level.getBlockState(pos);
        var block = state.getBlock();

        if (WeatheringIron.WAXABLES.containsKey(block)) {
            var newState = WeatheringIron.WAXABLES.get(block).defaultBlockState();
            level.setBlockAndUpdate(pos, newState);
            level.levelEvent(3003, pos, 0);
            stack.shrink(1);
            setSuccess(true);
            return stack;
        }

        return super.execute(source, stack);
    }
}
