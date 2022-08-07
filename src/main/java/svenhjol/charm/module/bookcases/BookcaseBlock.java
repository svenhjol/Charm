package svenhjol.charm.module.bookcases;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import svenhjol.charm.block.CharmBlockWithEntity;
import svenhjol.charm.enums.IWoodMaterial;
import svenhjol.charm.loader.CharmModule;

public class BookcaseBlock extends CharmBlockWithEntity {
    public static final IntegerProperty CAPACITY = IntegerProperty.create("capacity", 0, 3);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    protected CharmModule module;
    protected IWoodMaterial material;

    protected BookcaseBlock(CharmModule module, IWoodMaterial material) {
        super(module, material.getSerializedName() + "_bookcase", Properties.copy(Blocks.BOOKSHELF));

        this.module = module;
        this.material = material;

        registerDefaultState(defaultBlockState().setValue(CAPACITY, 0));

        if (material.isFlammable()) {
            this.setBurnTime(300);
        } else {
            this.setFireproof();
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BookcaseBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, CAPACITY);
    }

    @Override
    public CreativeModeTab getItemGroup() {
        return CreativeModeTab.TAB_DECORATIONS;
    }

    @Override
    public int getMaxStackSize() {
        var item = Bookcases.VALID_ITEMS.stream().reduce((i, j) -> i.getMaxStackSize() > j.getMaxStackSize() ? i : j);
        return item.map(Item::getMaxStackSize).orElse(1);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock() && level.getBlockEntity(pos) instanceof BookcaseBlockEntity bookcase) {
            Containers.dropContents(level, pos, bookcase);
            level.updateNeighbourForOutputSignal(pos, this);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide() && !player.isSpectator() && level.getBlockEntity(pos) instanceof BookcaseBlockEntity bookcase) {
            bookcase.unpackLootTable(player);
            player.openMenu(bookcase);
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.NORMAL;
    }


    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
    }
}
