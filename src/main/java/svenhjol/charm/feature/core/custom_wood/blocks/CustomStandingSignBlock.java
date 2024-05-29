package svenhjol.charm.feature.core.custom_wood.blocks;

import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.state.properties.WoodType;
import svenhjol.charm.charmony.iface.CustomWoodMaterial;

public class CustomStandingSignBlock extends StandingSignBlock {
    protected final CustomWoodMaterial material;

    public CustomStandingSignBlock(CustomWoodMaterial material, WoodType woodType) {
        super(woodType, material.blockProperties().strength(1.0F).noCollission());

        this.material = material;
    }
}
