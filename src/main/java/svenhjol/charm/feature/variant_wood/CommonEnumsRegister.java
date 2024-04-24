package svenhjol.charm.feature.variant_wood;

import net.minecraft.world.entity.vehicle.Boat;
import svenhjol.charm.foundation.Register;

public class CommonEnumsRegister extends Register<VariantWood> {
    public CommonEnumsRegister(VariantWood feature) {
        super(feature);
    }

    @Override
    public int priority() {
        return 100;
    }

    @SuppressWarnings("unused")
    @Override
    public void onRegister() {
        // Hack to inject the boat type enums early.
        var boatTypeValues = Boat.Type.values();
    }
}
