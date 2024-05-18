package svenhjol.charm.feature.player_pressure_plates.common;

import svenhjol.charm.feature.player_pressure_plates.PlayerPressurePlates;
import svenhjol.charm.foundation.feature.RegisterHolder;

import java.util.function.Supplier;

public final class Registers extends RegisterHolder<PlayerPressurePlates> {
    private static final String ID = "player_pressure_plate";

    public final Supplier<Block> block;
    public final Supplier<Block.BlockItem> blockItem;

    public Registers(PlayerPressurePlates feature) {
        super(feature);

        var registry = feature().registry();
        block = registry.block(ID, Block::new);
        blockItem = registry.item(ID, () -> new Block.BlockItem(block));
    }
}
