package svenhjol.charm.block;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WoodButtonBlock;
import net.minecraft.world.level.material.Material;
import svenhjol.charm.loader.CommonModule;

public abstract class CharmWoodenButtonBlock extends WoodButtonBlock implements ICharmBlock {
    private final CommonModule module;

    public CharmWoodenButtonBlock(CommonModule module, String name, Properties settings) {
        super(settings);

        this.register(module, name);
        this.module = module;
        this.setBurnTime(100);
    }

    public CharmWoodenButtonBlock(CommonModule module, String name) {
        this(module, name, Properties.of(Material.DECORATION)
            .strength(0.5F)
            .noCollission()
            .sound(SoundType.WOOD));
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
