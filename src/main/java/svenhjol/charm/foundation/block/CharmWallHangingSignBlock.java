package svenhjol.charm.foundation.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.state.properties.WoodType;
import svenhjol.charm.api.iface.IVariantWoodMaterial;

@SuppressWarnings("unused")
public class CharmWallHangingSignBlock extends WallHangingSignBlock {
    public <B extends Block> CharmWallHangingSignBlock(IVariantWoodMaterial material, B drops, WoodType woodType) {
        super(woodType, material.blockProperties()
            .strength(1.0F)
            .noCollission()
            .ignitedByLava()
            .dropsLike(drops));
    }
}
