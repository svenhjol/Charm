package svenhjol.charm.feature.suspicious_block_creating;

import svenhjol.charm.feature.suspicious_block_creating.common.Advancements;
import svenhjol.charm.feature.suspicious_block_creating.common.Handlers;
import svenhjol.charm.feature.suspicious_block_creating.common.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = "Use a piston to push an item into sand or gravel, making it suspicious.")
public final class SuspiciousBlockCreating extends CommonFeature {
    public final Registers registers;
    public final Handlers handlers;
    public final Advancements advancements;

    public SuspiciousBlockCreating(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
        advancements = new Advancements(this);
    }
}
