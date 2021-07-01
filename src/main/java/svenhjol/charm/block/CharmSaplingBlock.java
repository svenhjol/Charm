package svenhjol.charm.block;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.material.Material;
import svenhjol.charm.loader.CharmCommonModule;

public abstract class CharmSaplingBlock extends SaplingBlock implements ICharmBlock {
    private final CharmCommonModule module;

    public CharmSaplingBlock(CharmCommonModule module, String name, AbstractTreeGrower generator, Properties settings) {
        super(generator, settings);

        this.register(module, name);
        this.module = module;
        this.setBurnTime(100);
    }

    public CharmSaplingBlock(CharmCommonModule module, String name, AbstractTreeGrower generator) {
        this(module, name, generator, Properties.of(Material.PLANT)
            .noCollission()
            .randomTicks()
            .instabreak()
            .sound(SoundType.GRASS));
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
