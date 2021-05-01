package svenhjol.charm.block;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.init.CharmSounds;
import svenhjol.charm.base.block.CharmBlockWithEntity;
import svenhjol.charm.base.helper.ItemHelper;
import svenhjol.charm.base.helper.PlayerHelper;
import svenhjol.charm.blockentity.CookingPotBlockEntity;
import svenhjol.charm.module.CookingPots;

import java.util.Random;

public class CookingPotBlock extends CharmBlockWithEntity {
    private static final VoxelShape RAY_TRACE_SHAPE = createCuboidShape(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    private static final VoxelShape OUTLINE_SHAPE;

    public static IntProperty LIQUID = IntProperty.of("liquid", 0, 2);
    public static BooleanProperty HAS_FIRE = BooleanProperty.of("has_fire");

    public CookingPotBlock(CharmModule module) {
        super(module, "cooking_pot", Settings.of(Material.METAL, MapColor.STONE_GRAY)
            .requiresTool()
            .strength(2.0F)
            .nonOpaque());

        this.setDefaultState(this.getDefaultState()
            .with(HAS_FIRE, false)
            .with(LIQUID, 0));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack held = player.getStackInHand(hand);

        if (state.get(LIQUID) == 0 && held.getItem() == Items.WATER_BUCKET) {
            if (!world.isClient) {
                world.setBlockState(pos, state.with(LIQUID, 1), 3);
                player.setStackInHand(hand, new ItemStack(Items.BUCKET));
                world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 0.8F, 1.0F);
            }

            return ActionResult.success(world.isClient);
        }
        if (state.get(LIQUID) > 0 && state.get(HAS_FIRE)) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof CookingPotBlockEntity) {
                CookingPotBlockEntity pot = (CookingPotBlockEntity) blockEntity;

                if (!world.isClient) {
                    if (held.getItem() == Items.BOWL) {
                        ItemStack out = pot.take(world, pos, state, held);
                        if (out != null) {
                            PlayerHelper.addOrDropStack(player, out);

                            if (pot.portions > 0) {
                                world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_SPLASH, SoundCategory.BLOCKS, 0.5F, 1.0F);
                            } else {
                                world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.BLOCKS, 0.5F, 1.0F);
                            }
                        }

                    } else if (held.isFood()) {
                        ItemStack copy = held.copy(); // for checking if it's a bowl or bottle after adding to pot
                        boolean result = pot.add(world, pos, state, held);
                        if (result) {
                            world.playSound(null, pos, SoundEvents.ENTITY_FISHING_BOBBER_SPLASH, SoundCategory.BLOCKS, 0.5F, 1.0F);

                            if (!player.getAbilities().creativeMode) {
                                // if the food has a bowl, give it back to the player
                                if (ItemHelper.getBowlFoodItems().contains(copy.getItem()))
                                    PlayerHelper.addOrDropStack(player, new ItemStack(Items.BOWL));

                                // if the food has a bottle, give it back to the player
                                if (ItemHelper.getBottleFoodItems().contains(copy.getItem()))
                                    PlayerHelper.addOrDropStack(player, new ItemStack(Items.GLASS_BOTTLE));
                            }

                            // send message to client that an item was added
                            PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
                            data.writeLong(pos.asLong());
                            ServerPlayNetworking.send((ServerPlayerEntity) player, CookingPots.MSG_CLIENT_ADDED_TO_POT, data);
                        }
                    }
                }

                return ActionResult.success(world.isClient);
            }
        }

        return ActionResult.PASS;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return OUTLINE_SHAPE;
    }

    @Override
    public VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
        return RAY_TRACE_SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CookingPotBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : checkType(type, CookingPots.BLOCK_ENTITY, CookingPotBlockEntity::tick);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIQUID, HAS_FIRE);
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        CookingPotBlockEntity pot = this.getBlockEntity(world, pos);
        if (pot == null)
            return 0;

        if (pot.portions == 0)
            return 0;

        return Math.round((pot.portions / (float)CookingPotBlockEntity.MAX_PORTIONS) * 16);
    }

    @Nullable
    public CookingPotBlockEntity getBlockEntity(World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof CookingPotBlockEntity)
            return (CookingPotBlockEntity) blockEntity;

        return null;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        super.randomDisplayTick(state, world, pos, random);
        if (state.get(HAS_FIRE) && state.get(LIQUID) > 0 && random.nextInt(2) == 0) {
            world.addParticle(ParticleTypes.SMOKE, (double)pos.getX() + 0.13D + (0.7D * random.nextDouble()), (double)pos.getY() + 0.92D, (double)pos.getZ() + 0.13D + (0.7D * random.nextDouble()), 0.0D, 0.0D, 0.0D);

            if (random.nextInt(10) == 0) {
                world.playSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, CharmSounds.COOKING_POT, SoundCategory.BLOCKS, 0.25F + (0.25F * random.nextFloat()), random.nextFloat() * 0.7F + 0.6F, false);
            }
        }
    }

    static {
        OUTLINE_SHAPE = VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(), VoxelShapes.union(createCuboidShape(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D), createCuboidShape(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D), createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), RAY_TRACE_SHAPE), BooleanBiFunction.ONLY_FIRST);
    }
}
