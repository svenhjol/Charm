package svenhjol.charm.foundation.block;

import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.state.properties.WoodType;
import svenhjol.charm.api.iface.CustomWoodMaterial;

public class CharmStandingSignBlock extends StandingSignBlock {
    protected final CustomWoodMaterial material;

    public CharmStandingSignBlock(CustomWoodMaterial material, WoodType woodType) {
        super(woodType, material.blockProperties().strength(1.0F).noCollission());

        this.material = material;
    }
}
