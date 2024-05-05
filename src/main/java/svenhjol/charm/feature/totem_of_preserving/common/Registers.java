package svenhjol.charm.feature.totem_of_preserving.common;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.api.event.AnvilUpdateEvent;
import svenhjol.charm.api.event.PlayerInventoryDropEvent;
import svenhjol.charm.api.iface.ITotemInventoryCheckProvider;
import svenhjol.charm.api.iface.ITotemPreservingProvider;
import svenhjol.charm.feature.totem_of_preserving.TotemOfPreserving;
import svenhjol.charm.foundation.feature.Register;
import svenhjol.charm.foundation.helper.ApiHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class Registers extends Register<TotemOfPreserving> {
    public final Supplier<DataComponentType<Data>> data;
    public final Supplier<net.minecraft.world.item.Item> item;
    public final Supplier<net.minecraft.world.level.block.Block> block;
    public final Supplier<BlockEntityType<BlockEntity>> blockEntity;
    public final Supplier<SoundEvent> releaseSound;
    public final Supplier<SoundEvent> storeSound;
    public final List<ITotemPreservingProvider> preservingProviders = new ArrayList<>();
    public final List<ITotemInventoryCheckProvider> inventoryCheckProviders = new ArrayList<>();

    public Registers(TotemOfPreserving feature) {
        super(feature);
        var registry = feature.registry();

        data = registry.dataComponent("charm_totem_data",
            () -> builder -> builder
                .persistent(Data.CODEC)
                .networkSynchronized(Data.STREAM_CODEC));

        block = registry.block("totem_of_preserving_holder", Block::new);
        item = registry.item("totem_of_preserving", Item::new);

        blockEntity = registry.blockEntity("totem_block",
            () -> BlockEntity::new, List.of(block));

        releaseSound = registry.soundEvent("totem_release_items");
        storeSound = registry.soundEvent("totem_store_items");

        ApiHelper.registerProvider(new DataProviders());
    }

    @Override
    public void onRegister() {
    }

    @Override
    public void onEnabled() {
        ApiHelper.consume(ITotemPreservingProvider.class, preservingProviders::add);
        ApiHelper.consume(ITotemInventoryCheckProvider.class, inventoryCheckProviders::add);

        PlayerInventoryDropEvent.INSTANCE.handle(TotemOfPreserving.handlers::playerInventoryDrop);
        AnvilUpdateEvent.INSTANCE.handle(TotemOfPreserving.handlers::anvilUpdate);
    }
}
