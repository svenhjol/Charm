package svenhjol.charm.feature.potion_of_radiance;

import net.minecraft.core.Holder;
import net.minecraft.world.item.alchemy.Potion;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.feature.Register;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.Optional;
import java.util.function.Supplier;

public class PotionOfRadiance extends CommonFeature {
    static Supplier<Holder<Potion>> potion;
    static Supplier<Holder<Potion>> longPotion;

    @Override
    public String description() {
        return "Potion of Radiance gives the glowing effect. It is brewed using a torchflower.";
    }

    @Override
    public Optional<Register<? extends Feature>> registration() {
        return Optional.of(new CommonRegistration(this));
    }
}
