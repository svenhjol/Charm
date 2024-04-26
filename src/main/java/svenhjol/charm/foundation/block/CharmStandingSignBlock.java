package svenhjol.charm.foundation.block;

import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.state.properties.WoodType;
import svenhjol.charm.api.iface.IVariantWoodMaterial;

public class CharmStandingSignBlock extends StandingSignBlock {
    protected final IVariantWoodMaterial variantMaterial;

    public CharmStandingSignBlock(IVariantWoodMaterial material, WoodType woodType) {
        super(woodType, material.blockProperties().strength(1.0F).noCollission());

        this.variantMaterial = material;
    }
}
