package svenhjol.charm.feature.raid_horns;

import svenhjol.charm.feature.raid_horns.common.Advancements;
import svenhjol.charm.feature.raid_horns.common.Handlers;
import svenhjol.charm.feature.raid_horns.common.Providers;
import svenhjol.charm.feature.raid_horns.common.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = "Raid horns are sometimes dropped from raid leaders and can be used to call off raids or summon pillagers.")
public final class RaidHorns extends CommonFeature {
    public final Registers registers;
    public final Handlers handlers;
    public final Providers providers;
    public final Advancements advancements;

    public RaidHorns(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
        providers = new Providers(this);
        advancements = new Advancements(this);
    }
}
