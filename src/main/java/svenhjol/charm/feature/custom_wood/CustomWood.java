package svenhjol.charm.feature.custom_wood;

import svenhjol.charm.api.iface.IVariantWoodMaterial;
import svenhjol.charm.feature.custom_wood.common.CustomWoodDefinition;
import svenhjol.charm.feature.custom_wood.common.CustomWoodHolder;
import svenhjol.charm.feature.custom_wood.common.Handlers;
import svenhjol.charm.feature.custom_wood.common.Registers;
import svenhjol.charm.foundation.Resolve;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

import java.util.Optional;

@Feature(description = "Handles custom wood.")
public final class CustomWood extends CommonFeature {
    public final Registers registers;
    public final Handlers handlers;

    public CustomWood(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
    }

    @Override
    public boolean canBeDisabled() {
        return false;
    }

    /**
     * Helper method for other features to register custom wood types.
     */
    @SuppressWarnings("unused")
    public static void register(CommonFeature owner, CustomWoodDefinition definition) {
        Resolve.feature(CustomWood.class).registers.register(owner, definition);
    }

    /**
     * Helper method to get a custom wood definition for a material.
     */
    public static CustomWoodHolder holder(IVariantWoodMaterial material) {
        return Optional.of(Resolve.feature(CustomWood.class).registers.holders.get(material)).orElseThrow();
    }
}
