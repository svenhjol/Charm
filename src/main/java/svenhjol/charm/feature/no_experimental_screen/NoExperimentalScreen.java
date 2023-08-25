package svenhjol.charm.feature.no_experimental_screen;


import svenhjol.charm.CharmClient;
import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.base.CharmFeature;

@ClientFeature(
    mod = CharmClient.MOD_ID,
    description = "Prevents the 'Experimental World' screen from showing with customized worldgen or tags present."
)
public class NoExperimentalScreen extends CharmFeature { }
