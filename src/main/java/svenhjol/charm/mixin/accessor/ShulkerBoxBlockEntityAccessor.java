package svenhjol.charm.mixin.accessor;

import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import svenhjol.charm.base.iface.CharmMixin;

@Mixin(ShulkerBoxBlockEntity.class)
@CharmMixin(required = true)
public interface ShulkerBoxBlockEntityAccessor {
    @Accessor
    DefaultedList<ItemStack> getInventory();
}
