package svenhjol.charm.mixin.accessor;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractContainerScreen.class)
public interface AbstractContainerScreenAccessor {
    @Accessor("leftPos")
    int getLeftPos();

    @Accessor("topPos")
    int getTopPos();

    @Accessor("hoveredSlot")
    Slot getHoveredSlot();
}
