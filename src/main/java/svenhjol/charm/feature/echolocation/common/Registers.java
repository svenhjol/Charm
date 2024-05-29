package svenhjol.charm.feature.echolocation.common;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import svenhjol.charm.api.event.PlayerTickEvent;
import svenhjol.charm.feature.echolocation.Echolocation;
import svenhjol.charm.charmony.feature.RegisterHolder;

import java.util.function.Supplier;

public final class Registers extends RegisterHolder<Echolocation> {
    public final Supplier<Holder<MobEffect>> mobEffect;

    public Registers(Echolocation feature) {
        super(feature);

        mobEffect = feature.registry().mobEffect("echolocation", Effect::new);
    }

    @Override
    public void onEnabled() {
        PlayerTickEvent.INSTANCE.handle(feature().handlers::playerTick);
    }
}
