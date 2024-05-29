package svenhjol.charm.feature.mob_drops.chicken_drops;

import net.minecraft.sounds.SoundEvent;
import svenhjol.charm.charmony.feature.RegisterHolder;

import java.util.function.Supplier;

public final class Registers extends RegisterHolder<ChickenDrops> {
    public final Supplier<SoundEvent> shedFeatherSound;

    public Registers(ChickenDrops feature) {
        super(feature);

        shedFeatherSound = feature().registry().soundEvent("feather_shed");
    }
}
