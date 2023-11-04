package svenhjol.charm.feature.gentle_potion_particles;

import svenhjol.charmony.client.ClientFeature;

public class GentlePotionParticles extends ClientFeature {
    @Override
    public String description() {
        return "Potion effect particles are less obtrusive.";
    }

    @Override
    public boolean canBeDisabled() {
        return true;
    }
}
