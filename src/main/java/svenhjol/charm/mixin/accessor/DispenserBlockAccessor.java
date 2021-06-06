package svenhjol.charm.mixin.accessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import svenhjol.charm.annotation.CharmMixin;

import java.util.Map;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.DispenserBlock;

@Mixin(DispenserBlock.class)
@CharmMixin(required = true)
public interface DispenserBlockAccessor {
    /**
     * Used by OverrideHandler to access the dispenser behaviors.
     *
     * {@link svenhjol.charm.helper.OverrideHandler#changeDispenserBehavior(Item, Item)}
     */
    @Accessor("BEHAVIORS")
    static Map<Item, DispenseItemBehavior> getDispenseBehaviorRegistry() {
        throw new IllegalStateException();
    }
}
