package svenhjol.charm.charmony.common.helper;

import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;

public final class DispenserHelper {
    /**
     * Copypasta from DispenseItemBehavior.
     * @see net.minecraft.core.dispenser.DispenseItemBehavior
     */
    public static DefaultDispenseItemBehavior getDefaultDispenseBehavior() {
        return new DefaultDispenseItemBehavior() {
            @Override
            public ItemStack execute(BlockSource blockSource, ItemStack itemStack) {
                Direction direction = blockSource.getBlockState().getValue(DispenserBlock.FACING);
                EntityType<?> entityType = ((SpawnEggItem)itemStack.getItem()).getType(itemStack.getTag());
                try {
                    entityType.spawn(blockSource.getLevel(), itemStack, null, blockSource.getPos().relative(direction), MobSpawnType.DISPENSER, direction != Direction.UP, false);
                }
                catch (Exception exception) {
                    LOGGER.error("Error while dispensing spawn egg from dispenser at {}", blockSource.getPos(), exception);
                    return ItemStack.EMPTY;
                }
                itemStack.shrink(1);
                blockSource.getLevel().gameEvent(null, GameEvent.ENTITY_PLACE, blockSource.getPos());
                return itemStack;
            }
        };
    }
}
