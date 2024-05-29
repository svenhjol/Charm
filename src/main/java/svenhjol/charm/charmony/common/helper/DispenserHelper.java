package svenhjol.charm.charmony.common.helper;

import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
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
                Direction direction = blockSource.state().getValue(DispenserBlock.FACING);
                EntityType<?> entityType = ((SpawnEggItem)itemStack.getItem()).getType(itemStack);
                try {
                    entityType.spawn(blockSource.level(), itemStack, null, blockSource.pos().relative(direction), MobSpawnType.DISPENSER, direction != Direction.UP, false);
                }
                catch (Exception exception) {
                    LOGGER.error("Error while dispensing spawn egg from dispenser at {}", blockSource.pos(), exception);
                    return ItemStack.EMPTY;
                }
                itemStack.shrink(1);
                blockSource.level().gameEvent(null, GameEvent.ENTITY_PLACE, blockSource.pos());
                return itemStack;
            }
        };
    }
}
