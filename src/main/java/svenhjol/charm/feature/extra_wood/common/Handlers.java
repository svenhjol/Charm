package svenhjol.charm.feature.extra_wood.common;

import svenhjol.charm.feature.extra_wood.ExtraWood;
import svenhjol.charm.feature.extra_wood.azalea_wood.AzaleaWood;
import svenhjol.charm.foundation.feature.FeatureHolder;

import java.util.List;

public final class Handlers extends FeatureHolder<ExtraWood> {
    public Handlers(ExtraWood feature) {
        super(feature);
    }

    /**
     * Custom boat types to inject into the Boat.Type enum.
     * {@link net.minecraft.world.entity.vehicle.Boat.Type}
     * Register all feature boat type enums here!
     */
    public static final List<String> BOAT_TYPE_ENUMS = List.of(
        AzaleaWood.BOAT_ID
    );
}
