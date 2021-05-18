package svenhjol.charm.mixin.accessor;

import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import svenhjol.charm.base.iface.CharmMixin;

import java.util.Map;

@Mixin(DispenserBlock.class)
@CharmMixin(required = true)
public interface DispenserBlockAccessor {
    /**
     * Used by OverrideHandler to access the dispenser behaviors.
     *
     * {@link svenhjol.charm.base.helper.OverrideHandler#changeDispenserBehavior(Item, Item)}
     */
    @Accessor("BEHAVIORS")
    static Map<Item, DispenserBehavior> getDispenseBehaviorRegistry() {
        throw new IllegalStateException();
    }
}
