package svenhjol.charm.feature.grindstone_disenchanting.common;

import svenhjol.charm.charmony.event.GrindstoneEvents;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.grindstone_disenchanting.GrindstoneDisenchanting;

public final class Registers extends RegisterHolder<GrindstoneDisenchanting> {
    public Registers(GrindstoneDisenchanting feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        GrindstoneEvents.ON_TAKE.handle(feature().handlers::onTakeFromGrindstone);
        GrindstoneEvents.CALCULATE_OUTPUT.handle(feature().handlers::calculateGrindstoneOutput);
        GrindstoneEvents.CAN_TAKE.handle(feature().handlers::canTakeFromGrindstone);
        GrindstoneEvents.CAN_PLACE.handle(feature().handlers::canPlaceOnGrindstone);
    }
}
