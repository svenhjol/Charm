package svenhjol.charm.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.block.CharmBlockWithEntity;
import svenhjol.charm.base.handler.RegistryHandler;
import svenhjol.charm.blockentity.AstrolabeBlockEntity;
import svenhjol.charm.item.AstrolabeBlockItem;
import svenhjol.charm.module.Astrolabes;

import java.util.Optional;

public class AstrolabeBlock extends CharmBlockWithEntity {
    public static final VoxelShape LEGS;
    public static final VoxelShape ARMS1;
    public static final VoxelShape ARMS2;
    public static final VoxelShape CENTER;
    public static final VoxelShape SHAPE;

    public AstrolabeBlock(CharmModule module) {
        super(module, "astrolabe", Settings.copy(Blocks.COPPER_BLOCK));
    }

    @Override
    public void createBlockItem(Identifier id) {
        AstrolabeBlockItem blockItem = new AstrolabeBlockItem(this);
        RegistryHandler.item(id, blockItem);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        Optional<BlockPos> position = AstrolabeBlockItem.getPosition(itemStack);
        Optional<RegistryKey<World>> dimension = AstrolabeBlockItem.getDimension(itemStack);
        AstrolabeBlockEntity astrolabe = getBlockEntity(world, pos);

        if (position.isPresent() && dimension.isPresent() && astrolabe != null) {
            astrolabe.dimension = dimension.get();
            astrolabe.position = position.get();
            astrolabe.markDirty();
        }
        super.onPlaced(world, pos, state, placer, itemStack);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        AstrolabeBlockEntity astrolabe = getBlockEntity(world, pos);
        if (astrolabe != null) {
            if (!world.isClient) {
                ItemStack out = new ItemStack(Astrolabes.ASTROLABE);
                AstrolabeBlockItem.setDimension(out, astrolabe.dimension);
                AstrolabeBlockItem.setPosition(out, astrolabe.position);

                ItemEntity entity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), out);
                entity.setToDefaultPickupDelay();
                world.spawnEntity(entity);
            }
        }

        super.onBreak(world, pos, state, player);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new AstrolabeBlockEntity(pos, state);
    }

    @Nullable
    public AstrolabeBlockEntity getBlockEntity(World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof AstrolabeBlockEntity) {
            return (AstrolabeBlockEntity) blockEntity;
        }

        return null;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : checkType(type, Astrolabes.BLOCK_ENTITY, AstrolabeBlockEntity::tick);
    }

    static {
        LEGS = Block.createCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D);
        ARMS1 = Block.createCuboidShape(0.0D, 6.0D, 7.0D, 16.0D, 8.0D, 9.0D);
        ARMS2 = Block.createCuboidShape(7.0D, 6.0D, 0.0D, 9.0D, 8.0D, 16.0D);
        CENTER = Block.createCuboidShape(4.0D, 3.0D, 4.0D, 12.0D, 11.0D, 12.0D);
        SHAPE = VoxelShapes.union(LEGS, ARMS1, ARMS2, CENTER);
    }
}
