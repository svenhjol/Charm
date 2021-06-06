package svenhjol.charm.mixin.accessor;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import svenhjol.charm.annotation.CharmMixin;

@Mixin(ShulkerBoxBlockEntity.class)
@CharmMixin(required = true)
public interface ShulkerBoxBlockEntityAccessor {
    @Accessor
    NonNullList<ItemStack> getInventory();
}
