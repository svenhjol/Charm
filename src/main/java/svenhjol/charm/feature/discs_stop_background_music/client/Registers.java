package svenhjol.charm.feature.discs_stop_background_music.client;

import svenhjol.charm.charmony.event.BlockUseEvent;
import svenhjol.charm.charmony.event.ClientTickEvent;
import svenhjol.charm.charmony.event.SoundPlayEvent;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.discs_stop_background_music.DiscsStopBackgroundMusic;

public final class Registers extends RegisterHolder<DiscsStopBackgroundMusic> {
    public Registers(DiscsStopBackgroundMusic feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        BlockUseEvent.INSTANCE.handle(feature().handlers::blockUse);
        SoundPlayEvent.INSTANCE.handle(feature().handlers::soundPlay);
        ClientTickEvent.INSTANCE.handle(feature().handlers::clientTick);
    }
}
