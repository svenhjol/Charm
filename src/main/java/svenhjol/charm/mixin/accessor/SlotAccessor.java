package svenhjol.charm.mixin.accessor;

import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import svenhjol.charm.base.iface.CharmMixin;

@Mixin(Slot.class)
@CharmMixin(required = true)
public interface SlotAccessor {
    @Accessor
    int getIndex();
}
