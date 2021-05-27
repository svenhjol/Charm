package svenhjol.charm.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.helper.ModHelper;

import java.util.Arrays;
import java.util.List;

public abstract class CharmBlock extends Block implements ICharmBlock {
    public CharmModule module;
    private final List<String> loadedMods;

    public CharmBlock(CharmModule module, String name, AbstractBlock.Settings props, String... loadedMods) {
        super(props);
        this.module = module;
        this.loadedMods = Arrays.asList(loadedMods);
        register(module, name);
    }

    @Override
    public void addStacksForDisplay(ItemGroup group, DefaultedList<ItemStack> items) {
        if (enabled())
            super.addStacksForDisplay(group, items);
    }

    @Override
    public boolean enabled() {
        return module.enabled && loadedMods.stream().allMatch(ModHelper::isLoaded);
    }
}
