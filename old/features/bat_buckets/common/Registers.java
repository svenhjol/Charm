package svenhjol.charm.feature.bat_buckets.common;

import net.minecraft.sounds.SoundEvent;
import svenhjol.charm.api.CharmApi;
import svenhjol.charm.api.event.EntityUseEvent;
import svenhjol.charm.feature.bat_buckets.BatBuckets;
import svenhjol.charm.foundation.feature.Register;

import java.util.function.Supplier;

public final class Registers extends Register<BatBuckets> {
    public final Supplier<Item> bucketItem;
    public final Supplier<SoundEvent> grabSound;
    public final Supplier<SoundEvent> releaseSound;

    public Registers(BatBuckets feature) {
        super(feature);
        var registry = feature.registry();

        bucketItem = registry.item("bat_bucket", Item::new);
        grabSound = registry.soundEvent("bat_bucket_grab");
        releaseSound = registry.soundEvent("bat_bucket_release");

        CharmApi.registerProvider(new DataProviders());
    }

    @Override
    public void onEnabled() {
        EntityUseEvent.INSTANCE.handle(BatBuckets.handlers::useItemOnEntity);
    }
}
