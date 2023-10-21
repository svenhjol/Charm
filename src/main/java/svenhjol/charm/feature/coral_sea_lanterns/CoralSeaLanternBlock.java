package svenhjol.charm.feature.coral_sea_lanterns;

import net.minecraft.world.level.block.Block;
import svenhjol.charmony.base.CharmonyBlock;
import svenhjol.charmony.base.CharmonyBlockItem;
import svenhjol.charmony.common.CommonFeature;
import svenhjol.charmony_api.iface.IVariantMaterial;

import java.util.function.Supplier;

public class CoralSeaLanternBlock extends CharmonyBlock {
    public CoralSeaLanternBlock(CommonFeature feature, IVariantMaterial material) {
        super(feature, Properties.of()
            .strength(0.3F)
            .sound(material.soundType())
            .mapColor(material.mapColor())
            .lightLevel(blockState -> 15));
    }

    public static class BlockItem extends CharmonyBlockItem {
        public <T extends Block> BlockItem(CommonFeature feature, Supplier<T> block) {
            super(feature, block, new Properties());
        }
    }
}
