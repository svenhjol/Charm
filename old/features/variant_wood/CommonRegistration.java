package svenhjol.charm.feature.variant_wood;

import net.minecraft.world.entity.vehicle.Boat;
import svenhjol.charm.api.CharmApi;
import svenhjol.charm.api.event.EntityUseEvent;
import svenhjol.charm.foundation.feature.Register;

@SuppressWarnings("unused")
public final class CommonRegistration extends Register<VariantWood> {
    public CommonRegistration(VariantWood feature) {
        super(feature);

        // Hack to inject the boat type enums early.
        var boatTypeValues = Boat.Type.values();
    }

    @Override
    public void onRegister() {
        CustomChest.blockEntity = feature.registry().blockEntity("variant_chest", () -> VariantChestBlockEntity::new);
        CustomTrappedChest.blockEntity = feature.registry().blockEntity("variant_trapped_chest", () -> VariantTrappedChestBlockEntity::new);

        CharmApi.registerProvider(new DataProviders());
    }

    @Override
    public void onEnabled() {
        EntityUseEvent.INSTANCE.handle(AnimalInteraction::handle);
    }
}
