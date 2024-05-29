package svenhjol.charm.feature.bat_buckets;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;
import svenhjol.charm.feature.bat_buckets.common.Advancements;
import svenhjol.charm.feature.bat_buckets.common.Handlers;
import svenhjol.charm.feature.bat_buckets.common.Providers;
import svenhjol.charm.feature.bat_buckets.common.Registers;

@Feature(description = """
    Right-click a bat with a bucket to capture it.
    Right-click again to release it and locate entities around you.""")
public final class BatBuckets extends CommonFeature {
    public final Advancements advancements;
    public final Registers registers;
    public final Handlers handlers;
    public final Providers providers;

    public BatBuckets(CommonLoader loader) {
        super(loader);

        advancements = new Advancements(this);
        handlers = new Handlers(this);
        registers = new Registers(this);
        providers = new Providers(this);
    }
}
