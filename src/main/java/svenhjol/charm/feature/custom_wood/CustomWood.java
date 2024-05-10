package svenhjol.charm.feature.custom_wood;

import svenhjol.charm.feature.custom_wood.common.Registers;
import svenhjol.charm.feature.custom_wood.common.CustomWoodDefinition;
import svenhjol.charm.feature.custom_wood.common.Handlers;
import svenhjol.charm.foundation.Resolve;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(canBeDisabled = false, description = "Handles custom wood.")
public class CustomWood extends CommonFeature {

    public final Registers registers;
    public final Handlers handlers;

    public CustomWood(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
    }

    /**
     * Helper method for other features to register custom wood types.
     */
    @SuppressWarnings("unused")
    public static void register(CommonFeature feature, CustomWoodDefinition definition) {
        Resolve.feature(CustomWood.class).registers.register(feature, definition);
    }
}
