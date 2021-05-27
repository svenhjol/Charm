package svenhjol.charm.mixin.accessor;

import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import svenhjol.charm.annotation.CharmMixin;

@Mixin(BrewingRecipeRegistry.class)
@CharmMixin(required = true)
public interface BrewingRecipeRegistryAccessor {
    @Invoker
    static void invokeRegisterPotionRecipe(Potion input, Item item, Potion output) {
        throw new IllegalStateException();
    }
}
