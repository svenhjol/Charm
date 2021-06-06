package svenhjol.charm.mixin.accessor;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.ItemCombinerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import svenhjol.charm.annotation.CharmMixin;

@Mixin(ItemCombinerMenu.class)
@CharmMixin(required = true)
public interface ItemCombinerMenuAccessor {
    @Accessor
    Container getInputSlots();
}
