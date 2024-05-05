package svenhjol.charm.feature.totem_of_preserving;

import svenhjol.charm.api.event.AnvilUpdateEvent;
import svenhjol.charm.api.event.PlayerInventoryDropEvent;
import svenhjol.charm.api.iface.ITotemInventoryCheckProvider;
import svenhjol.charm.api.iface.ITotemPreservingProvider;
import svenhjol.charm.foundation.Registration;
import svenhjol.charm.foundation.helper.ApiHelper;

import java.util.List;

public final class CommonRegistration extends Registration<TotemOfPreserving> {
    public CommonRegistration(TotemOfPreserving feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        var registry = feature.registry();

        TotemOfPreserving.data = registry.dataComponent("charm_totem_data",
            () -> builder -> builder.persistent(TotemData.CODEC).networkSynchronized(TotemData.STREAM_CODEC));
        TotemOfPreserving.block = registry.block("totem_of_preserving_holder", TotemBlock::new);
        TotemOfPreserving.item = registry.item("totem_of_preserving", TotemItem::new);

        TotemOfPreserving.blockEntity = registry.blockEntity("totem_block",
            () -> TotemBlockEntity::new, List.of(TotemOfPreserving.block));

        TotemOfPreserving.releaseSound = registry.soundEvent("totem_release_items");
        TotemOfPreserving.storeSound = registry.soundEvent("totem_store_items");
    }

    @Override
    public void onEnabled() {
        ApiHelper.consume(ITotemPreservingProvider.class, provider -> TotemOfPreserving.preservingProviders.add(provider));
        ApiHelper.consume(ITotemInventoryCheckProvider.class, provider -> TotemOfPreserving.inventoryCheckProviders.add(provider));

        PlayerInventoryDropEvent.INSTANCE.handle(CommonHandlers::handlePlayerInventoryDrop);
        AnvilUpdateEvent.INSTANCE.handle(CommonHandlers::handleAnvilUpdate);
    }
}
