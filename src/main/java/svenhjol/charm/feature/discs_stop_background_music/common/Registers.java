package svenhjol.charm.feature.discs_stop_background_music.common;

import svenhjol.charm.api.event.BlockUseEvent;
import svenhjol.charm.api.event.ClientTickEvent;
import svenhjol.charm.api.event.SoundPlayEvent;
import svenhjol.charm.feature.discs_stop_background_music.DiscsStopBackgroundMusic;
import svenhjol.charm.foundation.feature.RegisterHolder;

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
