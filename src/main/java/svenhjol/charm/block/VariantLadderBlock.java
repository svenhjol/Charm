package svenhjol.charm.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.LadderBlock;
import svenhjol.meson.MesonModule;
import svenhjol.meson.block.IMesonBlock;
import svenhjol.meson.enums.IVariantMaterial;

public class VariantLadderBlock extends LadderBlock implements IMesonBlock {
    private final MesonModule module;

    public VariantLadderBlock(MesonModule module, IVariantMaterial type) {
        super(AbstractBlock.Settings.copy(Blocks.LADDER));
        register(module, type.asString() + "_ladder");

        /** @see net.minecraft.block.entity.AbstractFurnaceBlockEntity#createFuelTimeMap */
        this.setBurnTime(300);

        this.module = module;
    }

    @Override
    public boolean enabled() {
        return module.enabled;
    }
}
