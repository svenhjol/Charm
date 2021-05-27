package svenhjol.charm.module.copper_rails;

import net.minecraft.block.Material;
import net.minecraft.block.RailBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.collection.DefaultedList;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.block.ICharmBlock;

public class CopperRailBlock extends RailBlock implements ICharmBlock {
    private final CharmModule module;

    public CopperRailBlock(CharmModule module) {
        super(Settings.of(Material.DECORATION).noCollision().strength(0.7F).sounds(BlockSoundGroup.METAL));
        this.module = module;
        this.register(module, "copper_rail");
    }

    @Override
    public ItemGroup getItemGroup() {
        return ItemGroup.TRANSPORTATION;
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
