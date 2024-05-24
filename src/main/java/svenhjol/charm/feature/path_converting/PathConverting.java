package svenhjol.charm.feature.path_converting;

import svenhjol.charm.feature.path_converting.common.Handlers;
import svenhjol.charm.feature.path_converting.common.Registers;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = "Use a shovel or hoe to convert dirt and path blocks.")
public final class PathConverting extends CommonFeature {
    public final Registers registers;
    public final Handlers handlers;

    @Configurable(
        name = "Convert dirt to path",
        description = "If true, a shovel can be used to convert a dirt block to a path block."
    )
    private static boolean dirtToPath = true;

    @Configurable(
        name = "Convert path to dirt",
        description = "If true, a hoe can be used to convert a path block to a dirt block."
    )
    private static boolean pathToDirt = true;

    public PathConverting(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
    }

    public boolean dirtToPath() {
        return dirtToPath;
    }

    public boolean pathToDirt() {
        return pathToDirt;
    }
}
