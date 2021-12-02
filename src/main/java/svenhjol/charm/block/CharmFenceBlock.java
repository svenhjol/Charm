package svenhjol.charm.block;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import svenhjol.charm.loader.CharmModule;

public abstract class CharmFenceBlock extends FenceBlock implements ICharmBlock {
    private final CharmModule module;

    public CharmFenceBlock(CharmModule module, String name, Properties settings) {
        super(settings);

        this.register(module, name);
        this.module = module;
        this.setFireInfo(5, 20);
        this.setBurnTime(300);
    }

    public CharmFenceBlock(CharmModule module, String name, Block block) {
        this(module, name, Properties.of(Material.WOOD, block.defaultMaterialColor())
            .strength(2.0F, 3.0F)
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
