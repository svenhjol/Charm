package svenhjol.charm.mixin.accessor;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import svenhjol.charm.annotation.CharmMixin;

@Mixin(PotionBrewing.class)
@CharmMixin(required = true)
public interface BrewingRecipeRegistryAccessor {
    @Invoker
    static void invokeRegisterPotionRecipe(Potion input, Item item, Potion output) {
        throw new IllegalStateException();
    }
}
