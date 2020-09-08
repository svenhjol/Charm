package svenhjol.charm.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.LadderBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
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
    public void addStacksForDisplay(ItemGroup group, DefaultedList<ItemStack> list) {
        if (enabled())
            super.addStacksForDisplay(group, list);
    }

    @Override
    public boolean enabled() {
        return module.enabled;
    }
}
