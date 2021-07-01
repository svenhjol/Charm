package svenhjol.charm.block;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import svenhjol.charm.loader.CharmCommonModule;

public abstract class CharmFenceGateBlock extends FenceGateBlock implements ICharmBlock {
    private final CharmCommonModule module;

    public CharmFenceGateBlock(CharmCommonModule module, String name, Properties settings) {
        super(settings);

        this.register(module, name);
        this.module = module;
        this.setFireInfo(5, 20);
        this.setBurnTime(300);
    }

    public CharmFenceGateBlock(CharmCommonModule module, String name, Block block) {
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
