package svenhjol.charm.block;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import svenhjol.charm.loader.CharmModule;

public abstract class CharmWoodenSlabBlock extends SlabBlock implements ICharmBlock {
    private final CharmModule module;

    public CharmWoodenSlabBlock(CharmModule module, String name, Properties settings) {
        super(settings);
        this.register(module, name);
        this.module = module;
        this.setFireInfo(5, 20);
        this.setBurnTime(150);
    }

    public CharmWoodenSlabBlock(CharmModule module, String name, MaterialColor color) {
        this(module, name, Properties.of(Material.WOOD, color)
            .strength(2.0F, 3.0F)
            .sound(SoundType.WOOD));
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if (enabled()) {
            super.fillItemCategory(group, items);
        }
    }

    @Override
    public boolean enabled() {
        return module.isEnabled();
    }
}
