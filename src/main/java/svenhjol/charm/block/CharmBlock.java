package svenhjol.charm.block;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import svenhjol.charm.helper.ModHelper;
import svenhjol.charm.loader.CharmCommonModule;

import java.util.Arrays;
import java.util.List;

public abstract class CharmBlock extends Block implements ICharmBlock {
    public CharmCommonModule module;
    private final List<String> loadedMods;

    public CharmBlock(CharmCommonModule module, String name, BlockBehaviour.Properties props, String... loadedMods) {
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
        return module.isEnabled() && loadedMods.stream().allMatch(ModHelper::isLoaded);
    }
}
