package svenhjol.charm.feature.coral_sea_lanterns.common;

import svenhjol.charm.feature.coral_sea_lanterns.CoralSeaLanterns;
import svenhjol.charm.charmony.feature.RegisterHolder;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class Registers extends RegisterHolder<CoralSeaLanterns> {
    public final Map<Material, Supplier<Block>> blocks = new LinkedHashMap<>();
    public final Map<Material, Supplier<Block.BlockItem>> blockItems = new LinkedHashMap<>();

    public Registers(CoralSeaLanterns feature) {
        super(feature);

        var registry = feature().registry();

        for (var material : Material.values()) {
            var id = material.getSerializedName() + "_sea_lantern";
            var block = registry.block(id, () -> new Block(material));
            var blockItem = registry.item(id, () -> new Block.BlockItem(block));
            blocks.put(material, block);
            blockItems.put(material, blockItem);
        }
    }
}
