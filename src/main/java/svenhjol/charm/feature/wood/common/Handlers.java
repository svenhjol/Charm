package svenhjol.charm.feature.wood.common;

import svenhjol.charm.feature.wood.Wood;
import svenhjol.charm.feature.wood.azalea_wood.AzaleaWood;
import svenhjol.charm.foundation.feature.FeatureHolder;

import java.util.List;

public final class Handlers extends FeatureHolder<Wood> {
    public Handlers(Wood feature) {
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
