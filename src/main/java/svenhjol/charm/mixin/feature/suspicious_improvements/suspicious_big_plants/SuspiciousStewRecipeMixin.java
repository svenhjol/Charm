package svenhjol.charm.mixin.feature.suspicious_improvements.suspicious_big_plants;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.SuspiciousStewRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SuspiciousStewRecipe.class)
public class SuspiciousStewRecipeMixin {
    @Redirect(
        method = "matches(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/world/level/Level;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/tags/TagKey;)Z"
        )
    )
    private boolean redirectItemTagCheck(ItemStack stack, TagKey<Item> tag) {
        if (stack.is(Items.SUNFLOWER) || stack.is(Items.PITCHER_PLANT)) {
            return true;
        }
        return stack.is(tag); // default behavior
    }
}
