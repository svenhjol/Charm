package svenhjol.charm.charmony.common.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.state.properties.WoodType;
import svenhjol.charm.charmony.iface.CustomWoodMaterial;

@SuppressWarnings("unused")
public class CharmWallHangingSignBlock extends WallHangingSignBlock {
    public <B extends Block> CharmWallHangingSignBlock(CustomWoodMaterial material, B drops, WoodType woodType) {
        super(woodType, material.blockProperties()
            .strength(1.0F)
            .noCollission()
            .ignitedByLava()
            .dropsLike(drops));
    }
}
