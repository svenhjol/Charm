package svenhjol.charm.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import svenhjol.charm.module.VariantBarrels;
import svenhjol.meson.MesonModule;
import svenhjol.meson.block.IMesonBlock;
import svenhjol.meson.enums.IVariantMaterial;
import svenhjol.meson.mixin.accessor.BarrelBlockEntityAccessor;

import javax.annotation.Nullable;

public class VariantBarrelBlock extends BarrelBlock implements IMesonBlock {
    protected MesonModule module;
    protected IVariantMaterial type;

    public VariantBarrelBlock(MesonModule module, IVariantMaterial type) {
        super(AbstractBlock.Settings.copy(Blocks.BARREL));

        this.module = module;
        this.type = type;

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
        return module.enabled;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        BarrelBlockEntity barrel = BarrelBlockEntityAccessor.invokeConstructor(VariantBarrels.BLOCK_ENTITY);
        barrel.setCustomName(new TranslatableText("block." + this.module.mod.getId() + "." + type.asString() + "_barrel"));
        return barrel;
    }
}
