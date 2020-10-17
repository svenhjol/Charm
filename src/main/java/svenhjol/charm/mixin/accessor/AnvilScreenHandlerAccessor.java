package svenhjol.charm.mixin.accessor;

import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AnvilScreenHandler.class)
public interface AnvilScreenHandlerAccessor {
    @Accessor
    void setLevelCost(Property levelCost);

    @Accessor
    Property getLevelCost();

    @Accessor
    void setRepairItemUsage(int repairItemUsage);
}
