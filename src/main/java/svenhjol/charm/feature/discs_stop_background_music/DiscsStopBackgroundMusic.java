package svenhjol.charm.feature.discs_stop_background_music;

import svenhjol.charm.feature.discs_stop_background_music.client.Handlers;
import svenhjol.charm.feature.discs_stop_background_music.client.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;

@Feature(description = "Playing a music disc in a jukebox prevents background music from playing at the same time.")
public final class DiscsStopBackgroundMusic extends ClientFeature {
    public final Registers registers;
    public final Handlers handlers;

    public DiscsStopBackgroundMusic(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
    }

    @Override
    public boolean canBeDisabled() {
        return true;
    }
}
