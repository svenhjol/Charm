package svenhjol.charm.item;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.block.Block;
import svenhjol.charm.loader.CharmCommonModule;

public class CharmSignItem extends SignItem implements ICharmItem {
    private final CharmCommonModule module;

    public CharmSignItem(CharmCommonModule module, String name, Block standingBlock, Block wallBlock, Properties settings) {
        super(settings, standingBlock, wallBlock);

        this.register(module, name);
        this.module = module;
    }

    public CharmSignItem(CharmCommonModule module, String name, Block standingBlock, Block wallBlock) {
        this(module, name, standingBlock, wallBlock, new Item.Properties()
            .stacksTo(16)
            .tab(CreativeModeTab.TAB_DECORATIONS));
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
