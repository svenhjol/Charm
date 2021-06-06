package svenhjol.charm.mixin.accessor;

import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import svenhjol.charm.annotation.CharmMixin;

@Mixin(Item.class)
@CharmMixin(required = true)
public interface ItemAccessor {
    @Accessor
    int getMaxStackSize();

    @Mutable @Accessor
    void setMaxStackSize(int maxCount);

    @Accessor()
    void setDescriptionId(String key);
}
