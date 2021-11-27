package svenhjol.charm.mixin.core;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.core.Core;

@Mixin(AnvilMenu.class)
public class AllowZeroCostAnvilRecipesMixin {
    @Shadow @Final
    private DataSlot cost;

    /**
     * Vanilla doesn't allow taking an item from the anvil with a zero XP cost.
     * The return value from the method is false by default.
     */
    @Inject(
        method = "mayPickup",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookCanTakeOutput(Player player, boolean unused, CallbackInfoReturnable<Boolean> cir) {
        if (Core.allowZeroCostAnvilRecipes(player, cost)) {
            cir.setReturnValue(true);
        }
    }
}
