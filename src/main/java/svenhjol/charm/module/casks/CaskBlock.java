package svenhjol.charm.module.casks;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import svenhjol.charm.block.CharmBlockWithEntity;
import svenhjol.charm.helper.PlayerHelper;
import svenhjol.charm.init.CharmSounds;
import svenhjol.charm.module.CharmModule;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class CaskBlock extends CharmBlockWithEntity {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final VoxelShape X1, X2, X3, X4;
    public static final VoxelShape Y1, Y2, Y3, Y4;
    public static final VoxelShape Z1, Z2, Z3, Z4;
    public static final VoxelShape X_SHAPE;
    public static final VoxelShape Y_SHAPE;
    public static final VoxelShape Z_SHAPE;

    public CaskBlock(CharmModule module) {
        super(module, "cask", Properties.of(Material.WOOD)
            .strength(2.5F)
            .sound(SoundType.WOOD));

        this.registerDefaultState(this.defaultBlockState()
            .setValue(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        switch((state.getValue(FACING)).getAxis()) {
            case X:
            default:
                return X_SHAPE;
            case Z:
                return Z_SHAPE;
            case Y:
                return Y_SHAPE;
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        switch((state.getValue(FACING)).getAxis()) {
            case X:
            default:
                return X_SHAPE;
            case Z:
                return Z_SHAPE;
            case Y:
                return Y_SHAPE;
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack held = player.getItemInHand(hand);

        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof CaskBlockEntity) {
            CaskBlockEntity cask = (CaskBlockEntity) blockEntity;

            if (!world.isClientSide) {
                if (held.getItem() == Items.NAME_TAG && held.hasCustomHoverName()) {
                    cask.name = held.getHoverName().getContents();
                    cask.setChanged();
                    world.playSound(null, pos, SoundEvents.SMITHING_TABLE_USE, SoundSource.BLOCKS, 0.85F, 1.1F);
                    held.shrink(1);

                } else if (held.getItem() == Items.GLASS_BOTTLE) {
                    ItemStack out = cask.take(world, pos, state, held);
                    if (out != null) {
                        PlayerHelper.addOrDropStack(player, out);

                        if (cask.portions > 0) {
                            playCaskOpenSound(world, pos);
                            world.playSound(null, pos, SoundEvents.GENERIC_SPLASH, SoundSource.BLOCKS, 0.7F, 1.0F);
                        } else {
                            world.playSound(null, pos, SoundEvents.BARREL_CLOSE, SoundSource.BLOCKS, 0.5F, 1.0F);
                        }

                        // do advancement for taking brew
                        if (cask.portions > 1 && cask.effects.size() > 1) {
                            Casks.triggerTakenBrew((ServerPlayer) player);
                        }
                    }
                } else if (held.getItem() == Items.POTION) {
                    boolean result = cask.add(world, pos, state, held);
                    if (result) {
                        playCaskOpenSound(world, pos);
                        world.playSound(null, pos, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 0.9F, 0.9F);

                        // give the glass bottle back to the player
                        PlayerHelper.addOrDropStack(player, new ItemStack(Items.GLASS_BOTTLE));

                        // send message to client that an item was added
                        FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
                        data.writeLong(pos.asLong());
                        ServerPlayNetworking.send((ServerPlayer) player, Casks.MSG_CLIENT_ADDED_TO_CASK, data);

                        // do advancement for filling with potions
                        if (cask.portions > 1 && cask.effects.size() > 1)
                            Casks.triggerFilledWithPotion((ServerPlayer) player);
                    }
                }
            }

            return InteractionResult.sidedSuccess(world.isClientSide);
        }

        return InteractionResult.PASS;
    }

    @Override
    public CreativeModeTab getItemGroup() {
        return CreativeModeTab.TAB_DECORATIONS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (itemStack.hasCustomHoverName()) {
            CaskBlockEntity cask = getBlockEntity(world, pos);
            if (cask != null) {
                cask.name = itemStack.getHoverName().getContents();
                cask.setChanged();
            }
        }

        super.setPlacedBy(world, pos, state, placer, itemStack);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getNearestLookingDirection().getOpposite());
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
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CaskBlockEntity(pos, state);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        CaskBlockEntity cask = this.getBlockEntity(world, pos);
        if (cask == null)
            return 0;

        if (cask.portions == 0)
            return 0;

        return Math.round((cask.portions / (float) CaskBlockEntity.MAX_PORTIONS) * 16);
    }

    @Nullable
    public CaskBlockEntity getBlockEntity(Level world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof CaskBlockEntity)
            return (CaskBlockEntity) blockEntity;

        return null;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void animateTick(BlockState state, Level world, BlockPos pos, Random random) {
        if (random.nextInt(2) == 0) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof CaskBlockEntity) {
                List<MobEffect> effects = ((CaskBlockEntity) blockEntity).effects
                    .stream()
                    .map(Registry.MOB_EFFECT::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

                effects.forEach(effect -> {
                    int color = effect.getColor();
                    double r = (double) (color >> 16 & 255) / 255.0D;
                    double g = (double) (color >> 8 & 255) / 255.0D;
                    double b = (double) (color & 255) / 255.0D;
                    world.addParticle(ParticleTypes.AMBIENT_ENTITY_EFFECT, (double) pos.getX() + 0.13D + (0.7D * random.nextDouble()), (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.13D + (0.7D * random.nextDouble()), r, g, b);
                });

                if (!effects.isEmpty() && random.nextInt(20) == 0) {
                    world.playLocalSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, CharmSounds.CASK, SoundSource.BLOCKS, 0.1F + (0.1F * random.nextFloat()), random.nextFloat() * 0.7F + 0.6F, false);
                }
            }
        }
    }

    private void playCaskOpenSound(Level world, BlockPos pos) {
        world.playSound(null, pos, SoundEvents.BARREL_OPEN, SoundSource.BLOCKS, 0.6F, 1.0F);
    }

    static {
        X1 = Block.box(1.0D, 0.0D, 4.0D, 15.0D, 16.0D, 12.0D);
        X2 = Block.box(1.0D, 1.0D, 2.0D, 15.0D, 15.0D, 14.0D);
        X3 = Block.box(1.0D, 2.0D, 1.0D, 15.0D, 14.0D, 15.0D);
        X4 = Block.box(1.0D, 4.0D, 0.0D, 15.0D, 12.0D, 16.0D);
        Y1 = Block.box(4.0D, 1.0D, 0.0D, 12.0D, 15.0D, 16.0D);
        Y2 = Block.box(2.0D, 1.0D, 1.0D, 14.0D, 15.0D, 15.0D);
        Y3 = Block.box(1.0D, 1.0D, 2.0D, 15.0D, 15.0D, 14.0D);
        Y4 = Block.box(0.0D, 1.0D, 4.0D, 16.0D, 15.0D, 12.0D);
        Z1 = Block.box(4.0D, 0.0D, 1.0D, 12.0D, 16.0D, 15.0D);
        Z2 = Block.box(2.0D, 1.0D, 1.0D, 14.0D, 15.0D, 15.0D);
        Z3 = Block.box(1.0D, 2.0D, 1.0D, 15.0D, 14.0D, 15.0D);
        Z4 = Block.box(0.0D, 4.0D, 1.0D, 16.0D, 12.0D, 15.0D);
        X_SHAPE = Shapes.or(X1, X2, X3, X4);
        Y_SHAPE = Shapes.or(Y1, Y2, Y3, Y4);
        Z_SHAPE = Shapes.or(Z1, Z2, Z3, Z4);
    }
}
