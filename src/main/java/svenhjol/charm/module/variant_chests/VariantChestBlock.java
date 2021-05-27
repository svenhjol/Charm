package svenhjol.charm.module.variant_chests;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.block.ICharmBlock;
import svenhjol.charm.enums.IVariantMaterial;
import svenhjol.charm.helper.ModHelper;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class VariantChestBlock extends ChestBlock implements ICharmBlock, IVariantChestBlock {
    private final CharmModule module;
    private final IVariantMaterial type;
    private final List<String> loadedMods;

    public VariantChestBlock(CharmModule module, IVariantMaterial type, String... loadedMods) {
        this(module, type, Settings.copy(Blocks.CHEST), () -> VariantChests.NORMAL_BLOCK_ENTITY, loadedMods);
    }

    public VariantChestBlock(CharmModule module, IVariantMaterial type, AbstractBlock.Settings settings, Supplier<BlockEntityType<? extends ChestBlockEntity>> supplier, String... loadedMods) {
        super(settings, supplier);

        this.module = module;
        this.type = type;
        this.loadedMods = Arrays.asList(loadedMods);

        this.register(module, type.asString() + "_chest");
    }

    @Override
    public ItemGroup getItemGroup() {
        return ItemGroup.DECORATIONS;
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

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new VariantChestBlockEntity(pos, state);
    }

    @Override
    public IVariantMaterial getMaterialType() {
        return this.type;
    }
}
