package svenhjol.charm.feature.casks.common;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.casks.Casks;

import java.util.List;
import java.util.function.Supplier;

public final class Registers extends RegisterHolder<Casks> {
    static final String BLOCK_ID = "cask";
    public final Supplier<CaskBlock> block;
    public final Supplier<CaskBlock.BlockItem> blockItem;
    public final Supplier<BlockEntityType<CaskBlockEntity>> blockEntity;
    public final Supplier<DataComponentType<CaskData>> caskData;
    public final Supplier<DataComponentType<HomeBrewData>> homeBrewData;
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
        
        // Cask and homebrew item data.
        caskData = registry.dataComponent("cask",
            () -> builder -> builder
                .persistent(CaskData.CODEC)
                .networkSynchronized(CaskData.STREAM_CODEC));
        
        homeBrewData = registry.dataComponent("home_brew",
            () -> builder -> builder
                .persistent(HomeBrewData.CODEC)
                .networkSynchronized(HomeBrewData.STREAM_CODEC));

        // Casks can be burned for fuel
        registry.fuel(block);

        // Sounds
        addSound = registry.soundEvent("cask_add");
        emptySound = registry.soundEvent("cask_empty");
        nameSound = registry.soundEvent("cask_name");
        takeSound = registry.soundEvent("cask_take");
    }
}
