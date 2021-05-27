package svenhjol.charm.block;

import net.minecraft.block.LeavesBlock;
import net.minecraft.block.Material;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.collection.DefaultedList;
import svenhjol.charm.module.CharmModule;

public abstract class CharmLeavesBlock extends LeavesBlock implements ICharmBlock {
    private final CharmModule module;

    public CharmLeavesBlock(CharmModule module, String name, Settings settings) {
        super(settings);

        this.register(module, name);
        this.module = module;
    }

    public CharmLeavesBlock(CharmModule module, String name) {
        this(module, name, Settings.of(Material.LEAVES)
            .strength(0.2F)
            .ticksRandomly()
            .sounds(BlockSoundGroup.GRASS)
            .nonOpaque()
            .allowsSpawning((state, world, pos, type) -> false)
            .suffocates((state, world, pos) -> false)
            .blockVision((state, world, pos) -> false));
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
