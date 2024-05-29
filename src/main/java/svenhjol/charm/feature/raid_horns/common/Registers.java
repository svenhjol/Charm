package svenhjol.charm.feature.raid_horns.common;

import net.minecraft.sounds.SoundEvent;
import svenhjol.charm.api.event.EntityKilledDropEvent;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.raid_horns.RaidHorns;

import java.util.function.Supplier;

public final class Registers extends RegisterHolder<RaidHorns> {
    public final Supplier<RaidHornItem> item;
    public final Supplier<SoundEvent> callPatrolSound;
    public final Supplier<SoundEvent> callOffRaidSound;
    public final Supplier<SoundEvent> failSound;

    public Registers(RaidHorns feature) {
        super(feature);

        var registry = feature().registry();

        item = registry.item("raid_horn", RaidHornItem::new);
        callPatrolSound = registry.soundEvent("raid_horn_call_patrol");
        callOffRaidSound = registry.soundEvent("raid_horn_call_off_raid");
        failSound = registry.soundEvent("raid_horn_squeak");
    }

    @Override
    public void onEnabled() {
        EntityKilledDropEvent.INSTANCE.handle(feature().handlers::entityDrop);
    }
}
