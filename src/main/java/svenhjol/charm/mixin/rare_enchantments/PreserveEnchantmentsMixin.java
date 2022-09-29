package svenhjol.charm.mixin.rare_enchantments;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.module.rare_enchantments.RareEnchantments;

import java.util.Map;

@Mixin(AnvilMenu.class)
public abstract class PreserveEnchantmentsMixin extends ItemCombinerMenu {
    public PreserveEnchantmentsMixin(@Nullable MenuType<?> type, int syncId, Inventory playerInventory, ContainerLevelAccess context) {
        super(type, syncId, playerInventory, context);
    }

    @Redirect(
        method = "createResult",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/enchantment/Enchantment;isCompatibleWith(Lnet/minecraft/world/item/enchantment/Enchantment;)Z"
        )
    )
    private boolean hookCreateResult(Enchantment enchantment0, Enchantment enchantment1) {
        var slot0 = this.inputSlots.getItem(0);
        var slot1 = this.inputSlots.getItem(1);

        if (RareEnchantments.checkValidEnchantments(enchantment0, enchantment1, slot0, slot1)) {
            return true;
        }
        
        return enchantment0.isCompatibleWith(enchantment1);
    }
    
    /**
     * When trying to combine two items on the anvil where at least one of the items
     * has an enchantment level higher than the maximum, the additional levels are
     * lost and the output item defaults to the enchantment maximum.
     * This mixin redirects the set call to the EnchantmentHelper to a custom one.
     */
    @Redirect(
        method = "createResult",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;setEnchantments(Ljava/util/Map;Lnet/minecraft/world/item/ItemStack;)V"
        )
    )
    private void hookCreateResult(Map<Enchantment, Integer> enchantments, ItemStack outputStack) {
        var slot0 = this.inputSlots.getItem(0);
        var slot1 = this.inputSlots.getItem(1);
        
        RareEnchantments.preserveHighestLevelEnchantment(enchantments, slot0, slot1, outputStack);
    }
}