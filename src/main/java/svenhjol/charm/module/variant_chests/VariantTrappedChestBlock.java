package svenhjol.charm.module.variant_chests;

import svenhjol.charm.module.CharmModule;
import svenhjol.charm.block.ICharmBlock;
import svenhjol.charm.enums.IVariantMaterial;
import svenhjol.charm.helper.ModHelper;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.module.variant_chests.IVariantChestBlock;
import svenhjol.charm.module.variant_chests.VariantChests;
import svenhjol.charm.module.variant_chests.VariantTrappedChestBlockEntity;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings({"NullableProblems", "deprecation"})
public class VariantTrappedChestBlock extends ChestBlock implements ICharmBlock, IVariantChestBlock {
    private final CharmModule module;
    private final IVariantMaterial type;
    private final List<String> loadedMods;

    public VariantTrappedChestBlock(CharmModule module, IVariantMaterial type, String... loadedMods) {
        this(module, type, Properties.copy(Blocks.TRAPPED_CHEST), () -> VariantChests.TRAPPED_BLOCK_ENTITY);
    }

    public VariantTrappedChestBlock(CharmModule module, IVariantMaterial type, BlockBehaviour.Properties settings, Supplier<BlockEntityType<? extends ChestBlockEntity>> supplier, String... loadedMods) {
        super(settings, supplier);

        this.module = module;
        this.type = type;
        this.loadedMods = Arrays.asList(loadedMods);

        this.register(module, type.getSerializedName() + "_trapped_chest");
    }

    @Override
    public CreativeModeTab getItemGroup() {
        return CreativeModeTab.TAB_REDSTONE;
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

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new VariantTrappedChestBlockEntity(pos, state);
    }

    @Override
    public int getSignal(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
        return Mth.clamp(ChestBlockEntity.getOpenCount(world, pos), 0, 15);
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
        return direction == Direction.UP ? state.getSignal(world, pos, direction) : 0;
    }

    @Override
    public IVariantMaterial getMaterialType() {
        return type;
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    /**
     * Copypasta from {@link net.minecraft.world.level.block.TrappedChestBlock}
     */
    protected Stat<ResourceLocation> getOpenChestStat() {
        return Stats.CUSTOM.get(Stats.TRIGGER_TRAPPED_CHEST);
    }
}
