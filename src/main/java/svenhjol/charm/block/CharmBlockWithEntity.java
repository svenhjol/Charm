package svenhjol.charm.block;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.helper.ModHelper;
import svenhjol.charm.loader.CharmModule;

import java.util.Arrays;
import java.util.List;

public abstract class CharmBlockWithEntity extends BaseEntityBlock implements ICharmBlock {
    public CharmModule module;
    private final List<String> loadedMods;

    protected CharmBlockWithEntity(CharmModule module, String name, Properties props, String... loadedMods) {
        super(props);
        this.module = module;
        this.loadedMods = Arrays.asList(loadedMods);
        register(module, name);
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> list) {
        if (enabled())
            super.fillItemCategory(group, list);
    }

    @Override
    public boolean enabled() {
        return module.isEnabled() && loadedMods.stream().allMatch(ModHelper::isLoaded);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
