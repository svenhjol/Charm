package svenhjol.charm.feature.gentle_potion_particles;

import svenhjol.charm.CharmClient;
import svenhjol.charmony.annotation.ClientFeature;
import svenhjol.charmony.base.CharmonyFeature;

@ClientFeature(
    mod = CharmClient.MOD_ID,
    description = "Potion effect particles are less obtrusive."
)
public class GentlePotionParticles extends CharmonyFeature { }
