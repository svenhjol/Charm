package svenhjol.charm.mixin.accessor;

import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AnvilMenu.class)
public interface AnvilMenuAccessor {
    @Accessor
    void setCost(DataSlot levelCost);

    @Accessor
    DataSlot getCost();

    @Accessor
    void setRepairItemCountCost(int cost);
}
