package svenhjol.charm.feature.endermite_powder.common;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import svenhjol.charm.api.event.EntityKilledDropEvent;
import svenhjol.charm.feature.endermite_powder.EndermitePowder;
import svenhjol.charm.foundation.feature.RegisterHolder;

import java.util.function.Supplier;

public final class Registers extends RegisterHolder<EndermitePowder> {
    public static final String ID = "endermite_powder";
    public final Supplier<EntityType<Entity>> entity;
    public final Supplier<Item> item;
    public final Supplier<SoundEvent> launchSound;

    public Registers(EndermitePowder feature) {
        super(feature);

        var registry = feature().registry();
        item = registry.item(ID, Item::new);
        launchSound = registry.soundEvent("endermite_powder_launch");

        entity = registry.entity(ID, () -> EntityType.Builder
            .<Entity>of(Entity::new, MobCategory.MISC)
            .clientTrackingRange(80)
            .updateInterval(10)
            .sized(2.0F, 2.0F));
    }

    @Override
    public void onEnabled() {
        EntityKilledDropEvent.INSTANCE.handle(feature().handlers::entityKilledDrop);
    }
}
