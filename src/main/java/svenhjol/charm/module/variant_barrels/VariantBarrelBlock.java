package svenhjol.charm.module.variant_barrels;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.block.ICharmBlock;
import svenhjol.charm.enums.IVariantMaterial;
import svenhjol.charm.helper.ModHelper;
import svenhjol.charm.loader.CommonModule;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class VariantBarrelBlock extends BarrelBlock implements ICharmBlock {
    protected CommonModule module;
    protected IVariantMaterial type;
    private final List<String> loadedMods;

    public VariantBarrelBlock(CommonModule module, IVariantMaterial type, String... loadedMods) {
        this(module, type, BlockBehaviour.Properties.copy(Blocks.BARREL), loadedMods);
    }

    public VariantBarrelBlock(CommonModule module, IVariantMaterial material, BlockBehaviour.Properties settings, String... loadedMods) {
        super(settings);

        this.module = module;
        this.type = material;
        this.loadedMods = Arrays.asList(loadedMods);

        this.register(module, material.getSerializedName() + "_barrel");
        this.registerDefaultState(this.getStateDefinition()
            .any()
            .setValue(FACING, Direction.NORTH)
            .setValue(OPEN, false)
        );

        if (material.isFlammable()) {
            this.setBurnTime(300);
        } else {
            this.setFireproof();
        }
    }

    @Override
    public CreativeModeTab getItemGroup() {
        return CreativeModeTab.TAB_DECORATIONS;
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

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BarrelBlockEntity(pos, state);
    }
}
