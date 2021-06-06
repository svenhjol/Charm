package svenhjol.charm.block;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import svenhjol.charm.block.CharmBlock;
import svenhjol.charm.module.CharmModule;

public abstract class CharmPlanksBlock extends CharmBlock {
    public CharmPlanksBlock(CharmModule module, String name, Properties settings) {
        super(module, name, settings);
    }

    public CharmPlanksBlock(CharmModule module, String name, MaterialColor color) {
        this(module, name, Properties.of(Material.WOOD, color)
            .strength(2.0F, 3.0F)
            .sound(SoundType.WOOD));
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if (enabled())
            super.fillItemCategory(group, items);
    }
}
