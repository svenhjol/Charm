package svenhjol.charm.feature.smooth_glowstone;

import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Registration;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.Optional;
import java.util.function.Supplier;

public class SmoothGlowstone extends CommonFeature {
    static Supplier<SmoothGlowstoneBlock> block;
    static Supplier<SmoothGlowstoneBlock.BlockItem> blockItem;

    @Override
    public String description() {
        return "Smooth glowstone.";
    }

    @Override
    public Optional<Registration<? extends Feature>> registration() {
        return Optional.of(new CommonRegistration(this));
    }
}
