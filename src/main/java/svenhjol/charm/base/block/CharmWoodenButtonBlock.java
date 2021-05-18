package svenhjol.charm.base.block;

import net.minecraft.block.Material;
import net.minecraft.block.WoodenButtonBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.collection.DefaultedList;
import svenhjol.charm.base.CharmModule;

public abstract class CharmWoodenButtonBlock extends WoodenButtonBlock implements ICharmBlock {
    private final CharmModule module;

    public CharmWoodenButtonBlock(CharmModule module, String name, Settings settings) {
        super(settings);

        this.register(module, name);
        this.module = module;
    }

    public CharmWoodenButtonBlock(CharmModule module, String name) {
        this(module, name, Settings.of(Material.DECORATION)
            .strength(0.5F)
            .noCollision()
            .sounds(BlockSoundGroup.WOOD));
    }

    @Override
    public void addStacksForDisplay(ItemGroup group, DefaultedList<ItemStack> items) {
        if (enabled())
            super.addStacksForDisplay(group, items);
    }

    @Override
    public boolean enabled() {
        return module.enabled;
    }
}
