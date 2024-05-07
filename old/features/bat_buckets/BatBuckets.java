package svenhjol.charm.feature.bat_buckets;

import svenhjol.charm.feature.bat_buckets.common.Advancements;
import svenhjol.charm.feature.bat_buckets.common.Handlers;
import svenhjol.charm.feature.bat_buckets.common.Registers;
import svenhjol.charm.foundation.common.CommonFeature;

public class BatBuckets extends CommonFeature {
    public static Advancements advancements;
    public static Registers registers;
    public static Handlers handlers;

    @Override
    public String description() {
        return """
            Right-click a bat with a bucket to capture it.
            Right-click again to release it and locate entities around you.""";
    }

    @Override
    public void setup() {
        advancements = new Advancements(this);
        handlers = new Handlers(this);
        registers = new Registers(this);
    }
}
