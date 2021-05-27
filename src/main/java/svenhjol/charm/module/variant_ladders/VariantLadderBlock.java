package svenhjol.charm.module.variant_ladders;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.LadderBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.block.ICharmBlock;
import svenhjol.charm.enums.IVariantMaterial;

public class VariantLadderBlock extends LadderBlock implements ICharmBlock {
    private final CharmModule module;

    public VariantLadderBlock(CharmModule module, IVariantMaterial type) {
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
