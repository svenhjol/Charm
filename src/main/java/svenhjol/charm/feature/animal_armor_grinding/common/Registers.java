package svenhjol.charm.feature.animal_armor_grinding.common;


import net.minecraft.world.level.ItemLike;
import svenhjol.charm.api.event.GrindstoneEvents;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.animal_armor_grinding.AnimalArmorGrinding;

import java.util.HashMap;
import java.util.Map;

public final class Registers extends RegisterHolder<AnimalArmorGrinding> {
    public final Map<ItemLike, ItemLike> recipes = new HashMap<>();

    public Registers(AnimalArmorGrinding feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        GrindstoneEvents.CAN_PLACE.handle(feature().handlers::handleCanPlace);
        GrindstoneEvents.CALCULATE_OUTPUT.handle(feature().handlers::handleCalculateOutput);
        GrindstoneEvents.ON_TAKE.handle(feature().handlers::handleOnTake);
    }
}
