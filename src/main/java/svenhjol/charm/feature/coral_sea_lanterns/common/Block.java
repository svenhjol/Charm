package svenhjol.charm.feature.coral_sea_lanterns.common;

import svenhjol.charm.api.iface.CustomMaterial;
import svenhjol.charm.feature.coral_sea_lanterns.CoralSeaLanterns;
import svenhjol.charm.foundation.block.CharmBlock;

import java.util.function.Supplier;

public class Block extends CharmBlock<CoralSeaLanterns> {
    public Block(CustomMaterial material) {
        super(Properties.of()
            .strength(0.3F)
            .sound(material.soundType())
            .mapColor(material.mapColor())
            .lightLevel(blockState -> 15));
    }

    @Override
    public Class<CoralSeaLanterns> typeForFeature() {
        return CoralSeaLanterns.class;
    }

    public static class BlockItem extends net.minecraft.world.item.BlockItem {
        public <T extends net.minecraft.world.level.block.Block> BlockItem(Supplier<T> block) {
            super(block.get(), new Properties());
        }
    }
}
