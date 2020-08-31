package svenhjol.charm.block;

import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.BlockView;
import svenhjol.charm.blockentity.VariantChestBlockEntity;
import svenhjol.charm.module.VariantChests;
import svenhjol.meson.MesonModule;
import svenhjol.meson.block.IMesonBlock;
import svenhjol.meson.enums.IStorageMaterial;

import javax.annotation.Nullable;

@SuppressWarnings("NullableProblems")
public class VariantChestBlock extends ChestBlock implements IMesonBlock, IVariantChestBlock {
    private final MesonModule module;
    private final IStorageMaterial type;

    public VariantChestBlock(MesonModule module, IStorageMaterial type) {
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
    public BlockEntity createBlockEntity(BlockView worldIn) {
        VariantChestBlockEntity chest = new VariantChestBlockEntity();
        chest.setCustomName(new TranslatableText("block." + this.module.mod.getId() + "." + type.asString() + "_chest"));
        return chest;
    }

    @Override
    public IStorageMaterial getMaterialType() {
        return this.type;
    }
}
