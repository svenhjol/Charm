package svenhjol.charm.feature.item_frame_hiding.common;

import net.minecraft.core.particles.SimpleParticleType;
import svenhjol.charm.charmony.event.EntityAttackEvent;
import svenhjol.charm.charmony.event.EntityUseEvent;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.item_frame_hiding.ItemFrameHiding;

import java.util.function.Supplier;

public final class Registers extends RegisterHolder<ItemFrameHiding> {
    public final Supplier<SimpleParticleType> particleType;

    public Registers(ItemFrameHiding feature) {
        super(feature);
        var registry = feature.registry();

        particleType = registry.particleType("apply_amethyst", ParticleType::new);

        registry.serverPacketSender(Networking.S2CAddAmethyst.TYPE, Networking.S2CAddAmethyst.CODEC);
        registry.serverPacketSender(Networking.S2CRemoveAmethyst.TYPE, Networking.S2CRemoveAmethyst.CODEC);
    }

    @Override
    public void onEnabled() {
        EntityUseEvent.INSTANCE.handle(feature().handlers::entityUse);
        EntityAttackEvent.INSTANCE.handle(feature().handlers::entityAttack);
    }
}
