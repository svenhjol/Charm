package svenhjol.charm.feature.core.custom_wood.blocks;

import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.WoodType;
import svenhjol.charm.charmony.iface.CustomWoodMaterial;

public class CustomCeilingHangingSignBlock extends CeilingHangingSignBlock {
    protected final CustomWoodMaterial material;
    public CustomCeilingHangingSignBlock(CustomWoodMaterial material, WoodType woodType) {
        super(material.blockProperties().strength(1.0F).noCollission().sound(SoundType.HANGING_SIGN), woodType);
        this.material = material;
    }
}
