package svenhjol.charm.block;

import svenhjol.charm.block.ICharmBlock;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.helper.ModHelper;

import java.util.Arrays;
import java.util.List;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public abstract class CharmBlock extends Block implements ICharmBlock {
    public CharmModule module;
    private final List<String> loadedMods;

    public CharmBlock(CharmModule module, String name, BlockBehaviour.Properties props, String... loadedMods) {
        super(props);
        this.module = module;
        this.loadedMods = Arrays.asList(loadedMods);
        register(module, name);
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if (enabled())
            super.fillItemCategory(group, items);
    }

    @Override
    public boolean enabled() {
        return module.enabled && loadedMods.stream().allMatch(ModHelper::isLoaded);
    }
}
