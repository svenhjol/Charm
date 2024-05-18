package svenhjol.charm.feature.item_frame_hiding.common;

import net.minecraft.core.particles.SimpleParticleType;
import svenhjol.charm.api.event.EntityAttackEvent;
import svenhjol.charm.api.event.EntityUseEvent;
import svenhjol.charm.feature.item_frame_hiding.ItemFrameHiding;
import svenhjol.charm.foundation.feature.RegisterHolder;

import java.util.function.Supplier;

public final class Registers extends RegisterHolder<ItemFrameHiding> {
    public final Supplier<SimpleParticleType> particleType;

    public Registers(ItemFrameHiding feature) {
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
