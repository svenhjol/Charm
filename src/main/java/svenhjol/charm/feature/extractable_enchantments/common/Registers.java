package svenhjol.charm.feature.extractable_enchantments.common;

import svenhjol.charm.api.event.GrindstoneEvents;
import svenhjol.charm.feature.extractable_enchantments.ExtractableEnchantments;
import svenhjol.charm.foundation.feature.RegisterHolder;

public final class Registers extends RegisterHolder<ExtractableEnchantments> {
    public Registers(ExtractableEnchantments feature) {
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
