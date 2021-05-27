package svenhjol.charm.mixin.anvil_improvements;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.handler.ModuleHandler;
import svenhjol.charm.module.anvil_improvements.AnvilImprovements;

import java.util.Map;

@Mixin(AnvilScreenHandler.class)
public abstract class AllowHigherEnchantmentLevelMixin extends ForgingScreenHandler {
    public AllowHigherEnchantmentLevelMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
    }

    /**
     * When trying to combine two items on the anvil where at least one of the items
     * has an enchantment level higher than the maximum, the additional levels are
     * lost and the output item defaults to the enchantment maximum.
     *
     * This mixin redirects the set call to the EnchantmentHelper to a custom one
     * provided by AnvilImprovements.
     */
    @Redirect(
        method = "updateResult",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/enchantment/EnchantmentHelper;set(Ljava/util/Map;Lnet/minecraft/item/ItemStack;)V"
        )
    )
    private void hookUpdateResultAllowHigherLevel(Map<Enchantment, Integer> enchantments, ItemStack outputStack) {
        if (!ModuleHandler.enabled(AnvilImprovements.class) || !AnvilImprovements.higherEnchantmentLevels) {
            EnchantmentHelper.set(enchantments, outputStack); // vanilla behavior
            return;
        }

        ItemStack inputStack = this.input.getStack(1);
        AnvilImprovements.setEnchantmentsAllowHighLevel(enchantments, inputStack, outputStack);
    }
}
