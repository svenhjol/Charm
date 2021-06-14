package svenhjol.charm.mixin.accessor;

import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import svenhjol.charm.annotation.CharmMixin;

@Mixin(Slot.class)
@CharmMixin(required = true)
public interface SlotAccessor {
    @Accessor("index")
    int accessGetIndex();
}
