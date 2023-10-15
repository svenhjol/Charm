package svenhjol.charm.feature.potion_of_radiance;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import svenhjol.charm.Charm;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmonyFeature;

import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, description = "Potion of Radiance gives the glowing effect. It is brewed using a torchflower.")
public class PotionOfRadiance extends CharmonyFeature {
    static Supplier<Potion> potion;
    static Supplier<Potion> longPotion;

    @Override
    public void register() {
        var registry = Charm.instance().registry();

        potion = registry.potion("charm_radiance", RadiancePotion::new);
        longPotion = registry.potion("charm_long_radiance", LongRadiancePotion::new);
    }

    @Override
    public void runWhenEnabled() {
        var registry = Charm.instance().registry();

        registry.brewingRecipe(Potions.AWKWARD, Items.TORCHFLOWER, potion.get());
        registry.brewingRecipe(potion.get(), Items.REDSTONE, longPotion.get());
    }
}
