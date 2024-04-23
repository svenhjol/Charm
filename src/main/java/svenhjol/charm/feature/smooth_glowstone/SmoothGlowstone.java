package svenhjol.charm.feature.smooth_glowstone;

import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Register;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.List;
import java.util.function.Supplier;

public class SmoothGlowstone extends CommonFeature {
    static Supplier<SmoothGlowstoneBlock> block;
    static Supplier<SmoothGlowstoneBlock.BlockItem> blockItem;

    @Override
    public String description() {
        return "Smooth glowstone.";
    }

    @Override
    public List<? extends Register<? extends Feature>> register() {
        return List.of(new RegisterCommon(this));
    }
}
