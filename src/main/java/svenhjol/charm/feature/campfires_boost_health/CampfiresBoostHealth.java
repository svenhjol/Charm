package svenhjol.charm.feature.campfires_boost_health;

import svenhjol.charm.feature.campfires_boost_health.common.Advancements;
import svenhjol.charm.feature.campfires_boost_health.common.Handlers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = """
    Standing within range and sight of a lit campfire provides a small regeneration boost.
    It does not work if there are enemies nearby.""")
public final class CampfiresBoostHealth extends CommonFeature {
    public final Handlers handlers;
    public final Advancements advancements;

    public CampfiresBoostHealth(CommonLoader loader) {
        super(loader);

        handlers = new Handlers(this);
        advancements = new Advancements(this);
    }
}
