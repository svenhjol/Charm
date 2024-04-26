package svenhjol.charm.feature.potion_of_radiance;

import net.minecraft.core.Holder;
import net.minecraft.world.item.alchemy.Potion;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Register;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.List;

public class PotionOfRadiance extends CommonFeature {
    static Holder<Potion> potion;
    static Holder<Potion> longPotion;

    @Override
    public String description() {
        return "Potion of Radiance gives the glowing effect. It is brewed using a torchflower.";
    }

    @Override
    public List<? extends Register<? extends Feature>> register() {
        return List.of(new CommonRegister(this));
    }
}
