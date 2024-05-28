package svenhjol.charm.feature.casks.common;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.feature.casks.Casks;
import svenhjol.charm.foundation.feature.RegisterHolder;

import java.util.List;
import java.util.function.Supplier;

public final class Registers extends RegisterHolder<Casks> {
    static final String BLOCK_ID = "cask";
    public final Supplier<CaskBlock> block;
    public final Supplier<CaskBlock.BlockItem> blockItem;
    public final Supplier<BlockEntityType<CaskBlockEntity>> blockEntity;
    public final Supplier<DataComponentType<CaskData>> data;
    public final Supplier<SoundEvent> addSound;
    public final Supplier<SoundEvent> emptySound;
    public final Supplier<SoundEvent> nameSound;
    public final Supplier<SoundEvent> takeSound;

    public Registers(Casks feature) {
        super(feature);
        var registry = feature().registry();

        block = registry.block(BLOCK_ID, CaskBlock::new);
        blockItem = registry.item(BLOCK_ID, () -> new CaskBlock.BlockItem(block));
        blockEntity = registry.blockEntity(BLOCK_ID, () -> CaskBlockEntity::new, List.of(block));
        data = registry.dataComponent("cask",
            () -> builder -> builder
                .persistent(CaskData.CODEC)
                .networkSynchronized(CaskData.STREAM_CODEC));

        // Casks can be burned for fuel
        registry.fuel(block);

        // Sounds
        addSound = registry.soundEvent("cask_add");
        emptySound = registry.soundEvent("cask_empty");
        nameSound = registry.soundEvent("cask_name");
        takeSound = registry.soundEvent("cask_take");

        // Server to client packets
        registry.serverPacketSender(Networking.S2CAddedToCask.TYPE, Networking.S2CAddedToCask.CODEC);
    }
}
