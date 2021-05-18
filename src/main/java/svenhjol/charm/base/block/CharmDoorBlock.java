package svenhjol.charm.base.block;

import net.minecraft.block.Block;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.Material;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.collection.DefaultedList;
import svenhjol.charm.base.CharmModule;

public abstract class CharmDoorBlock extends DoorBlock implements ICharmBlock {
    private final CharmModule module;

    public CharmDoorBlock(CharmModule module, String name, Settings settings) {
        super(settings);

        this.register(module, name);
        this.module = module;
    }

    public CharmDoorBlock(CharmModule module, String name, Block block) {
        this(module, name, Settings.of(Material.WOOD, block.getDefaultMapColor())
            .strength(3.0F)
            .sounds(BlockSoundGroup.WOOD)
            .nonOpaque());
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
