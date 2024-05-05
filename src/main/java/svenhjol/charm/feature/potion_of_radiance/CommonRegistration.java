package svenhjol.charm.feature.potion_of_radiance;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import svenhjol.charm.foundation.feature.Register;

public final class CommonRegistration extends Register<PotionOfRadiance> {
    public CommonRegistration(PotionOfRadiance feature) {
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
            PotionOfRadiance.potion.get());

        registry.brewingRecipe(
            PotionOfRadiance.potion.get(),
            () -> Items.REDSTONE,
            PotionOfRadiance.longPotion.get());
    }
}
