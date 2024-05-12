package svenhjol.charm.feature.clear_item_frames.common;

import net.minecraft.core.particles.SimpleParticleType;
import svenhjol.charm.api.event.EntityAttackEvent;
import svenhjol.charm.api.event.EntityUseEvent;
import svenhjol.charm.feature.clear_item_frames.ClearItemFrames;
import svenhjol.charm.foundation.feature.RegisterHolder;

import java.util.function.Supplier;

public final class Registers extends RegisterHolder<ClearItemFrames> {
    public final Supplier<SimpleParticleType> particleType;

    public Registers(ClearItemFrames feature) {
        super(feature);
        var registry = feature.registry();

        particleType = registry.particleType("apply_amethyst", ParticleType::new);

        registry.serverPacketSender(Networking.AddAmethyst.TYPE, Networking.AddAmethyst.CODEC);
        registry.serverPacketSender(Networking.RemoveAmethyst.TYPE, Networking.RemoveAmethyst.CODEC);
    }

    @Override
    public void onEnabled() {
        EntityUseEvent.INSTANCE.handle(feature().handlers::entityUse);
        EntityAttackEvent.INSTANCE.handle(feature().handlers::entityAttack);
    }
}
