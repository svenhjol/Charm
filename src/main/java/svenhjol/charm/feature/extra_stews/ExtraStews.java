package svenhjol.charm.feature.extra_stews;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.longer_suspicious_effects.LongerSuspiciousEffects;
import svenhjol.charmony.annotation.Configurable;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmonyFeature;

@Feature(mod = Charm.MOD_ID, description = "Suspicious stews can be crafted from pitcher plants and sunflowers.")
public class ExtraStews extends CharmonyFeature {
    @Configurable(name = "Pitcher plant effect duration", description = "Number of seconds of strength and regeneration from a pitcher plant.")
    public static int pitcherPlantEffectDuration = 8;

    @Configurable(name = "Sunflower effect duration", description = "Number of seconds of health boost from a sunflower.")
    public static int sunflowerEffectDuration = 8;

    public static int getPitcherPlantEffectDuration() {
        return pitcherPlantEffectDuration * getMultiplier();
    }

    public static int getSunflowerEffectDuration() {
        return sunflowerEffectDuration * getMultiplier();
    }

    private static int getMultiplier() {
        return Charm.instance().loader().isEnabled(new ResourceLocation("charm:longer_suspicious_effects"))
            ? LongerSuspiciousEffects.effectMultiplier
            : 1;
    }
}
