package svenhjol.charm.block;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.init.CharmSounds;
import svenhjol.charm.base.block.CharmBlockWithEntity;
import svenhjol.charm.base.helper.PlayerHelper;
import svenhjol.charm.blockentity.CaskBlockEntity;
import svenhjol.charm.module.Casks;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class CaskBlock extends CharmBlockWithEntity {
    public static final DirectionProperty FACING = Properties.FACING;
    public static final VoxelShape X1, X2, X3, X4;
    public static final VoxelShape Y1, Y2, Y3, Y4;
    public static final VoxelShape Z1, Z2, Z3, Z4;
    public static final VoxelShape X_SHAPE;
    public static final VoxelShape Y_SHAPE;
    public static final VoxelShape Z_SHAPE;

    public CaskBlock(CharmModule module) {
        super(module, "cask", Settings.of(Material.WOOD)
            .strength(2.5F)
            .sounds(BlockSoundGroup.WOOD));

        this.setDefaultState(this.getDefaultState()
            .with(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        switch((state.get(FACING)).getAxis()) {
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
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        switch((state.get(FACING)).getAxis()) {
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
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack held = player.getStackInHand(hand);

        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof CaskBlockEntity) {
            CaskBlockEntity cask = (CaskBlockEntity) blockEntity;

            if (!world.isClient) {
                if (held.getItem() == Items.NAME_TAG && held.hasCustomName()) {
                    cask.name = held.getName().asString();
                    cask.markDirty();
                    world.playSound(null, pos, SoundEvents.BLOCK_SMITHING_TABLE_USE, SoundCategory.BLOCKS, 0.85F, 1.1F);

                    if (!player.isCreative())
                        held.decrement(1);

                } else if (held.getItem() == Items.GLASS_BOTTLE) {
                    ItemStack out = cask.take(world, pos, state, held);
                    if (out != null) {
                        PlayerHelper.addOrDropStack(player, out);

                        if (cask.portions > 0) {
                            playCaskOpenSound(world, pos);
                            world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_SPLASH, SoundCategory.BLOCKS, 0.7F, 1.0F);
                        } else {
                            world.playSound(null, pos, SoundEvents.BLOCK_BARREL_CLOSE, SoundCategory.BLOCKS, 0.5F, 1.0F);
                        }

                        // do advancement for taking brew
                        if (cask.portions > 1 && cask.effects.size() > 1) {
                            Casks.triggerTakenBrew((ServerPlayerEntity) player);
                        }
                    }
                } else if (held.getItem() == Items.POTION) {
                    boolean result = cask.add(world, pos, state, held);
                    if (result) {
                        playCaskOpenSound(world, pos);
                        world.playSound(null, pos, SoundEvents.BLOCK_BREWING_STAND_BREW, SoundCategory.BLOCKS, 0.9F, 0.9F);

                        // give the glass bottle back to the player
                        if (!player.getAbilities().creativeMode)
                            PlayerHelper.addOrDropStack(player, new ItemStack(Items.GLASS_BOTTLE));

                        // send message to client that an item was added
                        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
                        data.writeLong(pos.asLong());
                        ServerPlayNetworking.send((ServerPlayerEntity) player, Casks.MSG_CLIENT_ADDED_TO_CASK, data);

                        // do advancement for filling with potions
                        if (cask.portions > 1 && cask.effects.size() > 1) {
                            Casks.triggerFilledWithPotion((ServerPlayerEntity) player);
                        }
                    }
                }
            }

            return ActionResult.success(world.isClient);
        }

        return ActionResult.PASS;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (itemStack.hasCustomName()) {
            CaskBlockEntity cask = getBlockEntity(world, pos);
            if (cask != null) {
                cask.name = itemStack.getName().asString();
                cask.markDirty();
            }
        }

        super.onPlaced(world, pos, state, placer, itemStack);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerLookDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CaskBlockEntity(pos, state);
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        CaskBlockEntity cask = this.getBlockEntity(world, pos);
        if (cask == null)
            return 0;

        if (cask.portions == 0)
            return 0;

        return Math.round((cask.portions / (float)CaskBlockEntity.MAX_PORTIONS) * 16);
    }

    @Nullable
    public CaskBlockEntity getBlockEntity(World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof CaskBlockEntity)
            return (CaskBlockEntity) blockEntity;

        return null;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(2) == 0) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof CaskBlockEntity) {
                List<StatusEffect> effects = ((CaskBlockEntity) blockEntity).effects
                    .stream()
                    .map(Registry.STATUS_EFFECT::get)
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
                    world.playSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, CharmSounds.CASK, SoundCategory.BLOCKS, 0.1F + (0.1F * random.nextFloat()), random.nextFloat() * 0.7F + 0.6F, false);
                }
            }
        }
    }

    private void playCaskOpenSound(World world, BlockPos pos) {
        world.playSound(null, pos, SoundEvents.BLOCK_BARREL_OPEN, SoundCategory.BLOCKS, 0.6F, 1.0F);
    }

    static {
        X1 = Block.createCuboidShape(1.0D, 0.0D, 4.0D, 15.0D, 16.0D, 12.0D);
        X2 = Block.createCuboidShape(1.0D, 1.0D, 2.0D, 15.0D, 15.0D, 14.0D);
        X3 = Block.createCuboidShape(1.0D, 2.0D, 1.0D, 15.0D, 14.0D, 15.0D);
        X4 = Block.createCuboidShape(1.0D, 4.0D, 0.0D, 15.0D, 12.0D, 16.0D);
        Y1 = Block.createCuboidShape(4.0D, 1.0D, 0.0D, 12.0D, 15.0D, 16.0D);
        Y2 = Block.createCuboidShape(2.0D, 1.0D, 1.0D, 14.0D, 15.0D, 15.0D);
        Y3 = Block.createCuboidShape(1.0D, 1.0D, 2.0D, 15.0D, 15.0D, 14.0D);
        Y4 = Block.createCuboidShape(0.0D, 1.0D, 4.0D, 16.0D, 15.0D, 12.0D);
        Z1 = Block.createCuboidShape(4.0D, 0.0D, 1.0D, 12.0D, 16.0D, 15.0D);
        Z2 = Block.createCuboidShape(2.0D, 1.0D, 1.0D, 14.0D, 15.0D, 15.0D);
        Z3 = Block.createCuboidShape(1.0D, 2.0D, 1.0D, 15.0D, 14.0D, 15.0D);
        Z4 = Block.createCuboidShape(0.0D, 4.0D, 1.0D, 16.0D, 12.0D, 15.0D);
        X_SHAPE = VoxelShapes.union(X1, X2, X3, X4);
        Y_SHAPE = VoxelShapes.union(Y1, Y2, Y3, Y4);
        Z_SHAPE = VoxelShapes.union(Z1, Z2, Z3, Z4);
    }
}
