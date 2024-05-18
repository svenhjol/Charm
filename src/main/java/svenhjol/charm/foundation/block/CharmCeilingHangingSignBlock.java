package svenhjol.charm.foundation.block;

import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.WoodType;
import svenhjol.charm.api.iface.CustomWoodMaterial;

public class CharmCeilingHangingSignBlock extends CeilingHangingSignBlock {
    protected final CustomWoodMaterial material;
    public CharmCeilingHangingSignBlock(CustomWoodMaterial material, WoodType woodType) {
        super(woodType, material.blockProperties().strength(1.0F).noCollission().sound(SoundType.HANGING_SIGN));
        this.material = material;
    }
}
