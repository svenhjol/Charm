package svenhjol.charm.feature.grindable_horse_armor.common;


import net.minecraft.world.level.ItemLike;
import svenhjol.charm.api.event.GrindstoneEvents;
import svenhjol.charm.feature.grindable_horse_armor.GrindableHorseArmor;
import svenhjol.charm.foundation.feature.RegisterHolder;

import java.util.HashMap;
import java.util.Map;

public final class Registers extends RegisterHolder<GrindableHorseArmor> {
    public final Map<ItemLike, ItemLike> recipes = new HashMap<>();

    public Registers(GrindableHorseArmor feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        GrindstoneEvents.CAN_PLACE.handle(feature().handlers::handleCanPlace);
        GrindstoneEvents.CALCULATE_OUTPUT.handle(feature().handlers::handleCalculateOutput);
        GrindstoneEvents.ON_TAKE.handle(feature().handlers::handleOnTake);
    }
}
