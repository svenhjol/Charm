package svenhjol.charm.block;

import net.minecraft.block.Material;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.collection.DefaultedList;
import svenhjol.charm.module.CharmModule;

public abstract class CharmSaplingBlock extends SaplingBlock implements ICharmBlock {
    private final CharmModule module;

    public CharmSaplingBlock(CharmModule module, String name, SaplingGenerator generator, Settings settings) {
        super(generator, settings);

        this.register(module, name);
        this.module = module;
    }

    public CharmSaplingBlock(CharmModule module, String name, SaplingGenerator generator) {
        this(module, name, generator, Settings.of(Material.PLANT)
            .noCollision()
            .ticksRandomly()
            .breakInstantly()
            .sounds(BlockSoundGroup.GRASS));
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
