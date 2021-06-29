package svenhjol.charm.mixin.accessor;

import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Item.class)
public interface ItemAccessor {
    @Accessor
    int getMaxStackSize();

    @Mutable @Accessor
    void setMaxStackSize(int maxCount);

    @Accessor()
    void setDescriptionId(String key);
}
