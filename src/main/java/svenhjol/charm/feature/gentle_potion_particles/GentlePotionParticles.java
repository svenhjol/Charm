package svenhjol.charm.feature.gentle_potion_particles;

import svenhjol.charm.CharmClient;
import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.base.CharmFeature;

@ClientFeature(
    mod = CharmClient.MOD_ID,
    description = "Potion effect particles are less obtrusive."
)
public class GentlePotionParticles extends CharmFeature { }
