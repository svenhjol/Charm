package svenhjol.meson.mixin.accessor;

import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(DispenserBlock.class)
public interface DispenserBlockAccessor {
    /**
     * Used by Meson OverrideHandler to access the dispenser behaviors.
     *
     * {@link svenhjol.meson.helper.OverrideHandler#changeDispenserBehavior(Item, Item)}
     */
    @Accessor("BEHAVIORS")
    static Map<Item, DispenserBehavior> getDispenseBehaviorRegistry() {
        throw new IllegalStateException();
    }
}
