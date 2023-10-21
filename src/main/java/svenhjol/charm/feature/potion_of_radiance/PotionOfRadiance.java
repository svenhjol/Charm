package svenhjol.charm.feature.potion_of_radiance;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import svenhjol.charmony.common.CommonFeature;

import java.util.function.Supplier;

public class PotionOfRadiance extends CommonFeature {
    static Supplier<Potion> potion;
    static Supplier<Potion> longPotion;

    @Override
    public String description() {
        return "Potion of Radiance gives the glowing effect. It is brewed using a torchflower.";
    }

    @Override
    public void register() {
        var registry = mod().registry();

        potion = registry.potion("charm_radiance", RadiancePotion::new);
        longPotion = registry.potion("charm_long_radiance", LongRadiancePotion::new);

        registry.brewingRecipe(() -> Potions.AWKWARD, () -> Items.TORCHFLOWER, potion);
        registry.brewingRecipe(potion, () -> Items.REDSTONE, longPotion);
    }
}
