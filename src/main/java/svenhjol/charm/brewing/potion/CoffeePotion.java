package svenhjol.charm.brewing.potion;

import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potions;
import svenhjol.charm.brewing.module.Coffee;
import svenhjol.meson.MesonModule;
import svenhjol.meson.MesonPotion;

public class CoffeePotion extends MesonPotion {
    public CoffeePotion(MesonModule module) {
        super(module, "coffee_potion", new EffectInstance(Coffee.effect, Coffee.duration * 20));

        if (module.enabled) {
            registerRecipe(Potions.WATER, Items.COCOA_BEANS);
        }
    }
}
