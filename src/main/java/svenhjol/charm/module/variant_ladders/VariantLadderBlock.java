package svenhjol.charm.module.variant_ladders;

import svenhjol.charm.module.CharmModule;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import svenhjol.charm.block.ICharmBlock;
import svenhjol.charm.enums.IVariantMaterial;

public class VariantLadderBlock extends LadderBlock implements ICharmBlock {
    private final CharmModule module;

    public VariantLadderBlock(CharmModule module, IVariantMaterial type) {
        super(BlockBehaviour.Properties.copy(Blocks.LADDER));
        register(module, type.getSerializedName() + "_ladder");

        /** @see net.minecraft.block.entity.AbstractFurnaceBlockEntity#createFuelTimeMap */
        this.setBurnTime(300);

        this.module = module;
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
        return module.enabled;
    }
}
