package svenhjol.charm.feature.potion_of_radiance.common;

import net.minecraft.core.Holder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.potion_of_radiance.PotionOfRadiance;

import java.util.function.Supplier;

public final class Registers extends RegisterHolder<PotionOfRadiance> {
    public final Supplier<Holder<Potion>> potion;
    public final Supplier<Holder<Potion>> longPotion;

    public Registers(PotionOfRadiance feature) {
        super(feature);
        var registry = feature.registry();

        potion = registry.potion("radiance", RadiancePotion::new);
        longPotion = registry.potion("long_radiance", LongRadiancePotion::new);

        registry.brewingRecipe(
            Holder.direct(Potions.AWKWARD),
            () -> Items.TORCHFLOWER,
            potion.get());

        registry.brewingRecipe(
            potion.get(),
            () -> Items.REDSTONE,
            longPotion.get());
    }
}
