package svenhjol.charm.feature.no_experimental_screen;


import svenhjol.charmony.client.ClientFeature;

public class NoExperimentalScreen extends ClientFeature {
    @Override
    public String description() {
        return "Prevents the 'Experimental World' screen from showing with customized worldgen or tags present.";
    }
}