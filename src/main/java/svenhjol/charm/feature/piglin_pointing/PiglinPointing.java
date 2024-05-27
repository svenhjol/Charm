package svenhjol.charm.feature.piglin_pointing;

import svenhjol.charm.feature.piglin_pointing.common.Advancements;
import svenhjol.charm.feature.piglin_pointing.common.Handlers;
import svenhjol.charm.feature.piglin_pointing.common.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = """
    Piglins turn and point in the rough direction of a nether fortress or bastion remnant.
    By default, give them a chiseled nether brick block for a fortress and a gilded blackstone block for a bastion.
    The item tags 'piglin_barters_for_bastions' and 'piglin_barters_for_fortresses' can be used to configure the blocks.""")
public final class PiglinPointing extends CommonFeature {
    public final Registers registers;
    public final Handlers handlers;
    public final Advancements advancements;

    public PiglinPointing(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
        advancements = new Advancements(this);
    }
}
