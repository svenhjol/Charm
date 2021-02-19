package svenhjol.charm.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.block.ICharmBlock;
import svenhjol.charm.base.enums.IVariantMaterial;
import svenhjol.charm.base.helper.ModHelper;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class VariantBarrelBlock extends BarrelBlock implements ICharmBlock {
    protected CharmModule module;
    protected IVariantMaterial type;
    private final List<String> loadedMods;

    public VariantBarrelBlock(CharmModule module, IVariantMaterial type, String... loadedMods) {
        this(module, type, AbstractBlock.Settings.copy(Blocks.BARREL), loadedMods);
    }

    public VariantBarrelBlock(CharmModule module, IVariantMaterial type, AbstractBlock.Settings settings, String... loadedMods) {
        super(settings);

        this.module = module;
        this.type = type;
        this.loadedMods = Arrays.asList(loadedMods);

        this.register(module, type.asString() + "_barrel");
        this.setDefaultState(this.getStateManager()
            .getDefaultState()
            .with(FACING, Direction.NORTH)
            .with(OPEN, false)
        );
    }

    @Override
    public ItemGroup getItemGroup() {
        return ItemGroup.DECORATIONS;
    }

    @Override
    public void addStacksForDisplay(ItemGroup group, DefaultedList<ItemStack> list) {
        if (enabled())
            super.addStacksForDisplay(group, list);
    }

    @Override
    public boolean enabled() {
        return module.enabled && loadedMods.stream().allMatch(ModHelper::isLoaded);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BarrelBlockEntity(pos, state);
    }
}
