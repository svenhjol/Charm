package svenhjol.charm.feature.echolocation;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Registration;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.Optional;
import java.util.function.Supplier;

public class Echolocation extends CommonFeature {
    static Supplier<Holder<MobEffect>> mobEffect;

    @Override
    public String description() {
        return "A status effect that causes all living entities around the player to glow.";
    }

    @Override
    public boolean canBeDisabled() {
        return false; // Other features depend on this base feature.
    }

    @Override
    public Optional<Registration<? extends Feature>> registration() {
        return Optional.of(new CommonRegistration(this));
    }
}
