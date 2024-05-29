package svenhjol.charm.feature.campfires_heal_players;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;
import svenhjol.charm.feature.campfires_heal_players.common.Advancements;
import svenhjol.charm.feature.campfires_heal_players.common.Handlers;

@Feature(description = """
    Standing within range and sight of a lit campfire provides a small regeneration boost.
    It does not work if there are enemies nearby.""")
public final class CampfiresHealPlayers extends CommonFeature {
    public final Handlers handlers;
    public final Advancements advancements;

    public CampfiresHealPlayers(CommonLoader loader) {
        super(loader);

        handlers = new Handlers(this);
        advancements = new Advancements(this);
    }
}
