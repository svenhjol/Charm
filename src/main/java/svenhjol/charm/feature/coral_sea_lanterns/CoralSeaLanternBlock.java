package svenhjol.charm.feature.coral_sea_lanterns;

import net.minecraft.world.level.block.Block;
import svenhjol.charmapi.iface.IVariantMaterial;
import svenhjol.charmony.base.CharmBlock;
import svenhjol.charmony.base.CharmBlockItem;
import svenhjol.charmony.base.CharmFeature;

import java.util.function.Supplier;

public class CoralSeaLanternBlock extends CharmBlock {
    public CoralSeaLanternBlock(CharmFeature feature, IVariantMaterial material) {
        super(feature, Properties.of()
            .strength(0.3F)
            .sound(material.soundType())
            .mapColor(material.mapColor())
            .lightLevel(blockState -> 15));
    }

    public static class BlockItem extends CharmBlockItem {
        public <T extends Block> BlockItem(CharmFeature feature, Supplier<T> block) {
            super(feature, block, new Properties());
        }
    }
}
