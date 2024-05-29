package svenhjol.charm.feature.deepslate_dungeons;

import svenhjol.charm.feature.deepslate_dungeons.common.Handlers;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;

@Feature(description = "Dungeons in the deepslate layer will be constructed of deepslate bricks and cobbled deepslate.")
public final class DeepslateDungeons extends CommonFeature {
    public final Handlers handlers;

    public DeepslateDungeons(CommonLoader loader) {
        super(loader);

        handlers = new Handlers(this);
    }
}