package svenhjol.charm.feature.atlases.common;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import svenhjol.charm.api.event.PlayerLoginEvent;
import svenhjol.charm.api.event.PlayerTickEvent;
import svenhjol.charm.feature.atlases.Atlases;
import svenhjol.charm.foundation.feature.RegisterHolder;
import svenhjol.charm.foundation.helper.ApiHelper;

import java.util.function.Supplier;

public final class Registers extends RegisterHolder<Atlases> {
    public final Supplier<DataComponentType<AtlasData>> atlasData;
    public final Supplier<DataComponentType<MapData>> mapData;
    public final Supplier<Item> item;
    public final Supplier<MenuType<Menu>> menuType;
    public final Supplier<SoundEvent> openSound;
    public final Supplier<SoundEvent> closeSound;

    public Registers(Atlases feature) {
        super(feature);
        var registry = feature.registry();

        item = registry.item("atlas", Item::new);
        menuType = registry.menuType("atlas", () -> new MenuType<>(Menu::new, FeatureFlags.VANILLA_SET));
        openSound = registry.soundEvent("atlas_open");
        closeSound = registry.soundEvent("atlas_close");

        // Data components for the atlas and the map.
        atlasData = registry.dataComponent("charm_atlas_data",
            () -> builder -> builder
                .persistent(AtlasData.CODEC)
                .networkSynchronized(AtlasData.STREAM_CODEC));

        mapData = registry.dataComponent("charm_map_data",
            () -> builder -> builder
                .persistent(MapData.CODEC)
                .networkSynchronized(MapData.STREAM_CODEC));

        // Server packet senders
        registry.serverPacketSender(Networking.S2CSwappedAtlasSlot.TYPE,
            Networking.S2CSwappedAtlasSlot.CODEC);
        registry.serverPacketSender(Networking.S2CUpdateInventory.TYPE,
            Networking.S2CUpdateInventory.CODEC);

        // Client packet senders
        registry.clientPacketSender(Networking.C2SSwapAtlasSlot.TYPE,
            Networking.C2SSwapAtlasSlot.CODEC);
        registry.clientPacketSender(Networking.C2STransferAtlas.TYPE,
            Networking.C2STransferAtlas.CODEC);

        // Server packet receivers
        registry.packetReceiver(Networking.C2SSwapAtlasSlot.TYPE,
            () -> feature().handlers::swappedSlot);
        registry.packetReceiver(Networking.C2STransferAtlas.TYPE,
            () -> feature().handlers::transferAtlas);

        ApiHelper.registerProvider(new DataProviders(feature));
    }

    @Override
    public void onEnabled() {
        PlayerLoginEvent.INSTANCE.handle(feature().handlers::playerLogin);
        PlayerTickEvent.INSTANCE.handle(feature().handlers::playerTick);
    }
}
