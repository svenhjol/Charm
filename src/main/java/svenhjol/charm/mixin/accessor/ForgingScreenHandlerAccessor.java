package svenhjol.charm.mixin.accessor;

import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ForgingScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import svenhjol.charm.annotation.CharmMixin;

@Mixin(ForgingScreenHandler.class)
@CharmMixin(required = true)
public interface ForgingScreenHandlerAccessor {
    @Accessor
    Inventory getInput();
}
