package svenhjol.charm.feature.potion_of_radiance;

import net.minecraft.core.Holder;
import net.minecraft.world.item.alchemy.Potion;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Registration;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.Optional;

public class PotionOfRadiance extends CommonFeature {
    static Holder<Potion> potion;
    static Holder<Potion> longPotion;

    @Override
    public String description() {
        return "Potion of Radiance gives the glowing effect. It is brewed using a torchflower.";
    }

    @Override
    public Optional<Registration<? extends Feature>> registration() {
        return Optional.of(new CommonRegistration(this));
    }
}
