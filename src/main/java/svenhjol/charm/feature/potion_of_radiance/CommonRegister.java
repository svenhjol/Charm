package svenhjol.charm.feature.potion_of_radiance;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import svenhjol.charm.foundation.Register;

public class CommonRegister extends Register<PotionOfRadiance> {
    public CommonRegister(PotionOfRadiance feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        var registry = feature.registry();

        PotionOfRadiance.potion = registry.potion("charm_radiance", RadiancePotion::new);
        PotionOfRadiance.longPotion = registry.potion("charm_long_radiance", LongRadiancePotion::new);

        registry.brewingRecipe(
            Potions.AWKWARD,
            () -> Items.TORCHFLOWER,
            PotionOfRadiance.potion);

        registry.brewingRecipe(
            PotionOfRadiance.potion,
            () -> Items.REDSTONE,
            PotionOfRadiance.longPotion);
    }
}
