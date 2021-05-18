package svenhjol.charm.mixin.accessor;

import net.minecraft.entity.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import svenhjol.charm.base.iface.CharmMixin;

@Mixin(ItemEntity.class)
@CharmMixin(required = true)
public interface ItemEntityAccessor {
    @Accessor
    int getItemAge();
}