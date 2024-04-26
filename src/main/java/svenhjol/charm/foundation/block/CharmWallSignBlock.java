package svenhjol.charm.foundation.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.state.properties.WoodType;
import svenhjol.charm.api.iface.IVariantMaterial;

@SuppressWarnings("unused")
public class CharmWallSignBlock extends WallSignBlock {
    public <B extends Block> CharmWallSignBlock(IVariantMaterial material, B drops, WoodType woodType) {
        super(woodType, material.blockProperties()
            .strength(1.0F)
            .noCollission()
            .dropsLike(drops));
    }
}
