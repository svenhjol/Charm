package svenhjol.charm.module.cooking_pots;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import svenhjol.charm.block.CharmBlockWithEntity;
import svenhjol.charm.helper.ItemHelper;
import svenhjol.charm.helper.PlayerHelper;
import svenhjol.charm.init.CharmSounds;
import svenhjol.charm.loader.CharmCommonModule;

import java.util.Random;

public class CookingPotBlock extends CharmBlockWithEntity {
    private static final VoxelShape RAY_TRACE_SHAPE = box(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    private static final VoxelShape OUTLINE_SHAPE;

    public static IntegerProperty LIQUID = IntegerProperty.create("liquid", 0, 2);
    public static BooleanProperty HAS_FIRE = BooleanProperty.create("has_fire");

    public CookingPotBlock(CharmCommonModule module) {
        super(module, "cooking_pot", Properties.of(Material.METAL, MaterialColor.STONE)
            .requiresCorrectToolForDrops()
            .strength(2.0F)
            .noOcclusion());

        this.registerDefaultState(this.defaultBlockState()
            .setValue(HAS_FIRE, false)
            .setValue(LIQUID, 0));
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack held = player.getItemInHand(hand);

        if (state.getValue(LIQUID) == 0 && held.getItem() == Items.WATER_BUCKET) {
            if (!world.isClientSide) {
                world.setBlock(pos, state.setValue(LIQUID, 1), 3);

                if (!player.getAbilities().instabuild)
                    player.setItemInHand(hand, new ItemStack(Items.BUCKET));

                world.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 0.8F, 1.0F);
                CookingPots.triggerFilledWater((ServerPlayer) player);

                if (state.getValue(HAS_FIRE))
                    CookingPots.triggerLitFire((ServerPlayer) player);
            }

            return InteractionResult.sidedSuccess(world.isClientSide);
        }
        if (state.getValue(LIQUID) > 0 && state.getValue(HAS_FIRE)) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof CookingPotBlockEntity) {
                svenhjol.charm.module.cooking_pots.CookingPotBlockEntity pot = (CookingPotBlockEntity) blockEntity;

                if (!world.isClientSide) {
                    if (held.getItem() == Items.NAME_TAG && held.hasCustomHoverName()) {
                        pot.name = held.getHoverName().getContents();
                        pot.setChanged();
                        world.playSound(null, pos, SoundEvents.SMITHING_TABLE_USE, SoundSource.BLOCKS, 0.85F, 1.1F);
                        held.shrink(1);

                    } else if (held.getItem() == Items.BOWL) {
                        ItemStack out = pot.take(world, pos, state, held);
                        if (out != null) {
                            PlayerHelper.addOrDropStack(player, out);

                            if (pot.portions > 0) {
                                world.playSound(null, pos, SoundEvents.GENERIC_SPLASH, SoundSource.BLOCKS, 0.5F, 1.0F);
                            } else {
                                world.playSound(null, pos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 0.5F, 1.0F);
                            }

                            // do take food advancement
                            if (pot.portions > 0)
                                CookingPots.triggerTakenFood((ServerPlayer) player);
                        }

                    } else if (held.isEdible()) {
                        ItemStack copy = held.copy(); // for checking if it's a bowl or bottle after adding to pot
                        boolean result = pot.add(world, pos, state, held);
                        if (result) {
                            world.playSound(null, pos, SoundEvents.FISHING_BOBBER_SPLASH, SoundSource.BLOCKS, 0.5F, 1.0F);

                            // if the food has a bowl, give it back to the player
                            if (ItemHelper.getBowlFoodItems().contains(copy.getItem()))
                                PlayerHelper.addOrDropStack(player, new ItemStack(Items.BOWL));

                            // if the food has a bottle, give it back to the player
                            if (ItemHelper.getBottleFoodItems().contains(copy.getItem()))
                                PlayerHelper.addOrDropStack(player, new ItemStack(Items.GLASS_BOTTLE));

                            // send message to client that an item was added
                            FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
                            data.writeLong(pos.asLong());
                            ServerPlayNetworking.send((ServerPlayer) player, CookingPots.MSG_CLIENT_ADDED_TO_POT, data);

                            // do add items advancement
                            if (pot.portions > 0) {
                                CookingPots.triggerAddedItem((ServerPlayer) player);
                            }
                        }
                    }

                    // fire must be lit at this point so check the advancement
                    CookingPots.triggerLitFire((ServerPlayer) player);
                }

                return InteractionResult.sidedSuccess(world.isClientSide);
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return OUTLINE_SHAPE;
    }

    @Override
    public VoxelShape getInteractionShape(BlockState state, BlockGetter world, BlockPos pos) {
        return RAY_TRACE_SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CookingPotBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return world.isClientSide ? null : createTickerHelper(type, CookingPots.BLOCK_ENTITY, CookingPotBlockEntity::tick);
    }

    @Override
    public CreativeModeTab getItemGroup() {
        return CreativeModeTab.TAB_DECORATIONS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIQUID, HAS_FIRE);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        CookingPotBlockEntity pot = this.getBlockEntity(world, pos);
        if (pot == null)
            return 0;

        if (pot.portions == 0)
            return 0;

        return Math.round((pot.portions / (float) CookingPots.maxPortions) * 16);
    }

    @Nullable
    public CookingPotBlockEntity getBlockEntity(Level world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof CookingPotBlockEntity)
            return (CookingPotBlockEntity) blockEntity;

        return null;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void animateTick(BlockState state, Level world, BlockPos pos, Random random) {
        super.animateTick(state, world, pos, random);
        if (state.getValue(HAS_FIRE) && state.getValue(LIQUID) > 0 && random.nextInt(2) == 0) {
            world.addParticle(ParticleTypes.SMOKE, (double)pos.getX() + 0.13D + (0.7D * random.nextDouble()), (double)pos.getY() + 0.92D, (double)pos.getZ() + 0.13D + (0.7D * random.nextDouble()), 0.0D, 0.0D, 0.0D);

            if (random.nextInt(10) == 0) {
                world.playLocalSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, CharmSounds.COOKING_POT, SoundSource.BLOCKS, 0.25F + (0.25F * random.nextFloat()), random.nextFloat() * 0.7F + 0.6F, false);
            }
        }
    }

    static {
        OUTLINE_SHAPE = Shapes.join(Shapes.block(), Shapes.or(box(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D), box(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D), box(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), RAY_TRACE_SHAPE), BooleanOp.ONLY_FIRST);
    }
}
