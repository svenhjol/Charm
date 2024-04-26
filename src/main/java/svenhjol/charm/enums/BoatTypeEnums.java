package svenhjol.charm.enums;

import svenhjol.charm.feature.azalea_wood.AzaleaWood;
import java.util.List;

public class BoatTypeEnums {
    /**
     * Custom boat types to inject into the Boat.Type enum.
     * {@link net.minecraft.world.entity.vehicle.Boat.Type}
     */
    public static final List<String> BOAT_TYPE_ENUMS = List.of(
        AzaleaWood.BOAT_ID
    );
}
