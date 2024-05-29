package svenhjol.charm.feature.totem_of_preserving.common;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.api.event.AnvilUpdateEvent;
import svenhjol.charm.api.event.PlayerInventoryDropEvent;
import svenhjol.charm.feature.totem_of_preserving.TotemOfPreserving;
import svenhjol.charm.charmony.feature.RegisterHolder;

import java.util.List;
import java.util.function.Supplier;

public final class Registers extends RegisterHolder<TotemOfPreserving> {
    public final Supplier<DataComponentType<TotemData>> data;
    public final Supplier<net.minecraft.world.item.Item> item;
    public final Supplier<net.minecraft.world.level.block.Block> block;
    public final Supplier<BlockEntityType<BlockEntity>> blockEntity;
    public final Supplier<SoundEvent> releaseSound;
    public final Supplier<SoundEvent> storeSound;

    public Registers(TotemOfPreserving feature) {
        super(feature);
        var registry = feature.registry();

        data = registry.dataComponent("totem_of_preserving",
            () -> builder -> builder
                .persistent(TotemData.CODEC)
                .networkSynchronized(TotemData.STREAM_CODEC));

        block = registry.block("totem_of_preserving_holder", Block::new);
        item = registry.item("totem_of_preserving", Item::new);

        blockEntity = registry.blockEntity("totem_block",
            () -> BlockEntity::new, List.of(block));

        releaseSound = registry.soundEvent("totem_release_items");
        storeSound = registry.soundEvent("totem_store_items");
    }

    @Override
    public void onEnabled() {
        PlayerInventoryDropEvent.INSTANCE.handle(feature().handlers::playerInventoryDrop);
        AnvilUpdateEvent.INSTANCE.handle(feature().handlers::anvilUpdate);
    }
}
