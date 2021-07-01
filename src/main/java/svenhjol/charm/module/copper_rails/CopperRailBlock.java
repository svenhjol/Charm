package svenhjol.charm.module.copper_rails;

import svenhjol.charm.loader.CommonModule;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import svenhjol.charm.block.ICharmBlock;

public class CopperRailBlock extends RailBlock implements ICharmBlock {
    private final CommonModule module;

    public CopperRailBlock(CommonModule module) {
        super(Properties.of(Material.DECORATION).noCollission().strength(0.7F).sound(SoundType.METAL));
        this.module = module;
        this.register(module, "copper_rail");
    }

    @Override
    public CreativeModeTab getItemGroup() {
        return CreativeModeTab.TAB_TRANSPORTATION;
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if (enabled())
            super.fillItemCategory(group, items);
    }

    @Override
    public boolean enabled() {
        return module.isEnabled();
    }
}
