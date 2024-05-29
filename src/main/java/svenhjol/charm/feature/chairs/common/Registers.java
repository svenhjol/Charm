package svenhjol.charm.feature.chairs.common;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import svenhjol.charm.charmony.event.BlockUseEvent;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.chairs.Chairs;

import java.util.function.Supplier;

public final class Registers extends RegisterHolder<Chairs> {
    public final Supplier<EntityType<Chair>> entity;

    public Registers(Chairs feature) {
        super(feature);

        entity = feature().registry().entity("chair", () -> EntityType.Builder
            .<Chair>of(Chair::new, MobCategory.MISC)
            .sized(0.25f, 0.25f)
            .clientTrackingRange(1)
            .updateInterval(1));
    }

    @Override
    public void onEnabled() {
        BlockUseEvent.INSTANCE.handle(feature().handlers::blockUse);
    }
}
