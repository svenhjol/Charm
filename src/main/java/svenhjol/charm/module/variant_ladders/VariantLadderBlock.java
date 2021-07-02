package svenhjol.charm.module.variant_ladders;

import svenhjol.charm.loader.CharmModule;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import svenhjol.charm.block.ICharmBlock;
import svenhjol.charm.enums.IWoodMaterial;

public class VariantLadderBlock extends LadderBlock implements ICharmBlock {
    private final CharmModule module;

    public VariantLadderBlock(CharmModule module, IWoodMaterial material) {
        super(BlockBehaviour.Properties.copy(Blocks.LADDER));
        register(module, material.getSerializedName() + "_ladder");

        this.module = module;

        if (material.isFlammable()) {
            this.setBurnTime(300);
        } else {
            this.setFireproof();
        }
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> list) {
        if (enabled())
            super.fillItemCategory(group, list);
    }

    @Override
    public CreativeModeTab getItemGroup() {
        return CreativeModeTab.TAB_DECORATIONS;
    }

    @Override
    public boolean enabled() {
        return module.isEnabled();
    }
}
