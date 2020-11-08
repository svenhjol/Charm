package svenhjol.charm.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockView;
import svenhjol.charm.blockentity.VariantTrappedChestBlockEntity;
import svenhjol.charm.module.VariantChests;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.block.ICharmBlock;
import svenhjol.charm.base.enums.IVariantMaterial;

import javax.annotation.Nullable;

@SuppressWarnings({"NullableProblems", "deprecation"})
public class VariantTrappedChestBlock extends ChestBlock implements ICharmBlock, IVariantChestBlock {
    private final CharmModule module;
    private final IVariantMaterial type;

    public VariantTrappedChestBlock(CharmModule module, IVariantMaterial type) {
        super(Settings.copy(Blocks.TRAPPED_CHEST), () -> VariantChests.TRAPPED_BLOCK_ENTITY);

        this.module = module;
        this.type = type;

        this.register(module, type.asString() + "_trapped_chest");
    }

    @Override
    public ItemGroup getItemGroup() {
        return ItemGroup.REDSTONE;
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
        VariantTrappedChestBlockEntity chest = new VariantTrappedChestBlockEntity(pos, state);
        chest.setCustomName(new TranslatableText("block." + module.mod + "." + type.asString() + "_trapped_chest"));

        return chest;
    }

    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return MathHelper.clamp(ChestBlockEntity.getPlayersLookingInChestCount(world, pos), 0, 15);
    }

    @Override
    public int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return direction == Direction.UP ? state.getWeakRedstonePower(world, pos, direction) : 0;
    }

    @Override
    public IVariantMaterial getMaterialType() {
        return type;
    }

    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    /**
     * Copypasta from {@link net.minecraft.block.TrappedChestBlock}
     */
    protected Stat<Identifier> getOpenStat() {
        return Stats.CUSTOM.getOrCreateStat(Stats.TRIGGER_TRAPPED_CHEST);
    }
}
