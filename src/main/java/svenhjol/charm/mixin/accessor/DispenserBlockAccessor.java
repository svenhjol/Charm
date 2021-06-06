package svenhjol.charm.mixin.accessor;

import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.DispenserBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import svenhjol.charm.annotation.CharmMixin;

import java.util.Map;

@Mixin(DispenserBlock.class)
@CharmMixin(required = true)
public interface DispenserBlockAccessor {
    @Accessor("DISPENSER_REGISTRY")
    static Map<Item, DispenseItemBehavior> getDispenserRegistry() {
        throw new IllegalStateException();
    }
}
