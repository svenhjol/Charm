package svenhjol.charm.module.bookcases;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import svenhjol.charm.block.CharmBlockWithEntity;
import svenhjol.charm.enums.IVariantMaterial;
import svenhjol.charm.loader.CharmModule;

import javax.annotation.Nullable;

public class BookcaseBlock extends CharmBlockWithEntity {
    public static final IntegerProperty SLOTS = IntegerProperty.create("slots", 0, BookcaseBlockEntity.SIZE);

    protected CharmModule module;
    protected IVariantMaterial type;

    public BookcaseBlock(CharmModule module, IVariantMaterial material, String... loadedMods) {
        this(module, material, BlockBehaviour.Properties.copy(Blocks.BOOKSHELF), loadedMods);
    }

    public BookcaseBlock(CharmModule module, IVariantMaterial material, BlockBehaviour.Properties settings, String... loadedMods) {
        super(module, material.getSerializedName() + "_bookcase", settings, loadedMods);

        this.module = module;
        this.type = material;

        registerDefaultState(defaultBlockState().setValue(SLOTS, 0));

        if (material.isFlammable()) {
            this.setBurnTime(300);
        } else {
            this.setFireproof();
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide && !player.isSpectator()) {

            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof BookcaseBlockEntity) {
                BookcaseBlockEntity bookcase = (BookcaseBlockEntity)blockEntity;
                bookcase.unpackLootTable(player);
                player.openMenu(bookcase);
            }
        }
        return InteractionResult.CONSUME;
    }


    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity tile = world.getBlockEntity(pos);
            if (tile instanceof BookcaseBlockEntity) {
                Containers.dropContents(world, pos, (Container) tile);
                world.updateNeighbourForOutputSignal(pos, this);
            }
        }
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (stack.hasCustomHoverName()) {
            BlockEntity tile = world.getBlockEntity(pos);
            if (tile instanceof BookcaseBlockEntity)
                ((BookcaseBlockEntity) tile).setCustomName(stack.getHoverName());
        }
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.NORMAL;
    }

    @Override
    public CreativeModeTab getItemGroup() {
        return CreativeModeTab.TAB_DECORATIONS;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        return state.getValue(SLOTS);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BookcaseBlockEntity(pos, state);
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(SLOTS);
    }
}
