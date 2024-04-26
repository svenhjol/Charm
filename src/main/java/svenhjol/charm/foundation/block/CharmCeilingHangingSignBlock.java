package svenhjol.charm.foundation.block;

import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.WoodType;
import svenhjol.charm.api.iface.IVariantWoodMaterial;

public class CharmCeilingHangingSignBlock extends CeilingHangingSignBlock {
    protected final IVariantWoodMaterial variantMaterial;
    public CharmCeilingHangingSignBlock(IVariantWoodMaterial material, WoodType woodType) {
        super(woodType, material.blockProperties().strength(1.0F).noCollission().sound(SoundType.HANGING_SIGN));
        this.variantMaterial = material;
    }
}
