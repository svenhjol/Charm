package svenhjol.charm.block;

import svenhjol.charm.block.ICharmBlock;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.helper.ModHelper;

import java.util.Arrays;
import java.util.List;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

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
        return module.enabled && loadedMods.stream().allMatch(ModHelper::isLoaded);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
