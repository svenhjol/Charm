package svenhjol.charm.feature.revive_pets.common;

import net.minecraft.core.component.DataComponentType;
import svenhjol.charm.api.event.EntityKilledEvent;
import svenhjol.charm.api.event.ItemUseEvent;
import svenhjol.charm.feature.revive_pets.RevivePets;
import svenhjol.charm.foundation.feature.RegisterHolder;

import java.util.function.Supplier;

public final class Registers extends RegisterHolder<RevivePets> {
    public final Supplier<DataComponentType<Data>> data;

    public Registers(RevivePets feature) {
        super(feature);

        data = feature().registry().dataComponent("charm_revive_pet_data",
            () -> builder -> builder
                .persistent(Data.CODEC)
                .networkSynchronized(Data.STREAM_CODEC));
    }

    public DataComponentType<Data> data() {
        return data.get();
    }

    @Override
    public void onEnabled() {
        EntityKilledEvent.INSTANCE.handle(feature().handlers::entityKilled);
        ItemUseEvent.INSTANCE.handle(feature().handlers::itemUsed);
    }
}
