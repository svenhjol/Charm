package svenhjol.charm.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.block.ICharmBlock;
import svenhjol.charm.base.enums.IVariantMaterial;
import svenhjol.charm.blockentity.VariantChestBlockEntity;
import svenhjol.charm.module.VariantChests;

import javax.annotation.Nullable;

@SuppressWarnings("NullableProblems")
public class VariantChestBlock extends ChestBlock implements ICharmBlock, IVariantChestBlock {
    private final CharmModule module;
    private final IVariantMaterial type;

    public VariantChestBlock(CharmModule module, IVariantMaterial type) {
        super(Settings.copy(Blocks.CHEST), () -> VariantChests.NORMAL_BLOCK_ENTITY);

        this.module = module;
        this.type = type;

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
        return module.enabled;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        VariantChestBlockEntity chest = new VariantChestBlockEntity(pos, state);
        chest.setCustomName(new TranslatableText("block." + module.mod + "." + type.asString() + "_chest"));
        return chest;
    }

    @Override
    public IVariantMaterial getMaterialType() {
        return this.type;
    }
}
