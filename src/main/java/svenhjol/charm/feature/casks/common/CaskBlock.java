package svenhjol.charm.feature.casks.common;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import svenhjol.charm.api.iface.FuelProvider;
import svenhjol.charm.feature.casks.Casks;
import svenhjol.charm.foundation.feature.FeatureResolver;

import java.util.Objects;
import java.util.function.Supplier;

public class CaskBlock extends BaseEntityBlock implements FuelProvider, FeatureResolver<Casks> {
    private static final DirectionProperty FACING = BlockStateProperties.FACING;
    private static final MapCodec<CaskBlock> CODEC = simpleCodec(CaskBlock::new);
    private static final VoxelShape X1, X2, X3, X4;
    private static final VoxelShape Y1, Y2, Y3, Y4;
    private static final VoxelShape Z1, Z2, Z3, Z4;
    private static final VoxelShape X_SHAPE;
    private static final VoxelShape Y_SHAPE;
    private static final VoxelShape Z_SHAPE;
    private static final int FUEL_TIME = 300;

    public CaskBlock() {
        this(Properties.of()
            .strength(2.5f)
            .sound(SoundType.WOOD));

        registerDefaultState(defaultBlockState()
            .setValue(FACING, Direction.NORTH));
    }

    private CaskBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player,
                                              InteractionHand hand, BlockHitResult hitResult) {
        var blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CaskBlockEntity cask) {
            if (!level.isClientSide) {
                if (stack.getItem() == Items.NAME_TAG && stack.has(DataComponents.CUSTOM_NAME)) {

                    // Name the cask using a name tag.
                    cask.name = stack.getHoverName();
                    cask.setChanged();

                    level.playSound(null, pos, feature().registers.nameSound.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
                    stack.shrink(1);

                } else if (stack.is(Items.GLASS_BOTTLE)) {

                    // Take a bottle of liquid from the cask using a glass bottle.
                    var out = cask.take();
                    if (out != null) {
                        player.getInventory().add(out);

                        stack.shrink(1);

                        if (cask.effects.size() > 1) {
                            feature().advancements.tookLiquidFromCask(player);
                        }
                    }

                } else if (feature().handlers.isValidPotion(stack)) {

                    // Add a bottle of liquid to the cask using a filled glass bottle.
                    var result = cask.add(stack);
                    if (result) {
                        stack.shrink(1);

                        // give the glass bottle back to the player
                        player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));

                        // Let nearby players know an item was added to the cask
                        Networking.S2CAddedToCask.send((ServerLevel) level, pos);

                        // do advancement for filling with potions
                        if (cask.bottles > 1 && cask.effects.size() > 1) {
                            feature().advancements.addedLiquidToCask(player);
                        }
                    }
                }

                return ItemInteractionResult.SUCCESS;
            }
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (level.getBlockEntity(pos) instanceof CaskBlockEntity cask) {
            if (stack.has(DataComponents.CUSTOM_NAME)) {
                cask.name = stack.getHoverName();
            }
        }

        super.setPlacedBy(level, pos, state, placer, stack);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CaskBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntity) {
        if (level.isClientSide) {
            return null;
        }
        return CaskBlock.createTickerHelper(blockEntity, feature().registers.blockEntity.get(), CaskBlockEntity::serverTick);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState().setValue(FACING, ctx.getNearestLookingDirection().getOpposite());
    }

    @Override
    public int fuelTime() {
        return FUEL_TIME;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof CaskBlockEntity cask && cask.bottles > 0) {
            return Math.round((cask.bottles / (float) feature().maxBottles()) * 16);
        }
        return 0;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch ((state.getValue(CaskBlock.FACING)).getAxis()) {
            default -> X_SHAPE;
            case Z -> Z_SHAPE;
            case Y -> Y_SHAPE;
        };
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch ((state.getValue(CaskBlock.FACING)).getAxis()) {
            default -> X_SHAPE;
            case Z -> Z_SHAPE;
            case Y -> Y_SHAPE;
        };
    }

    @Override
    protected RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }
//
//    @Override
//    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> list, TooltipFlag tooltipFlag) {
//        super.appendHoverText(stack, context, list, tooltipFlag);
//
//        var tag = BlockItem.getBlockEntityData(stack);
//        if (tag != null) {
//
//            // Dumb hack, don't know why "id" isn't serialized via the loot table drop yet.
//            // Witho ut it the call to BlockEntity.loadStatic fails.
//            tag.put("id", StringTag.valueOf("strange:cask"));
//
//            var blockEntity = BlockEntity.loadStatic(BlockPos.ZERO, feature().registers.block.get().defaultBlockState(), tag);
//            if (blockEntity instanceof CaskBlockEntity cask && cask.bottles > 0) {
//                list.add(Component.translatable("gui.strange.cask.portions", cask.bottles).withStyle(ChatFormatting.AQUA));
//
//                if (!cask.effects.isEmpty()) {
//                    for (var effect : cask.effects) {
//                        BuiltInRegistries.MOB_EFFECT.getOptional(effect).ifPresent(
//                            mobEffect -> list.add(Component.translatable(mobEffect.getDescriptionId()).withStyle(ChatFormatting.BLUE)));
//                    }
//                } else {
//                    list.add(Component.translatable("gui.strange.cask.only_contains_water").withStyle(ChatFormatting.BLUE));
//                }
//            }
//        }
//    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextDouble() < 0.7d) {
            var blockEntity = level.getBlockEntity(pos);

            if (blockEntity instanceof CaskBlockEntity cask && cask.bottles > 0) {
                var effects = cask.effects
                    .stream()
                    .map(BuiltInRegistries.MOB_EFFECT::get)
                    .filter(Objects::nonNull)
                    .toList();

                if (effects.isEmpty()) {
                    // There's only water in the cask.
                    createWaterParticle(level, pos);
                } else {
                    // Create particles for each effect color
                    effects.forEach(effect -> createEffectParticle(level, pos, effect));
                }
            }
        }
    }

    void createWaterParticle(Level level, BlockPos pos) {
        var random = level.getRandom();

        level.addParticle(ParticleTypes.DRIPPING_WATER,
            pos.getX() + random.nextDouble(),
            pos.getY() + 0.7d,
            pos.getZ() + random.nextDouble(),
            0.0d, 0.0d, 0.0d);
    }

    void createEffectParticle(Level level, BlockPos pos, MobEffect effect) {
        var random = level.getRandom();
        var color = effect.getColor();

        var r = (color >> 16 & 255) / 255.0f;
        var g = (color >> 8 & 255) / 255.0f;
        var b = (color & 255) / 255.0f;

        level.addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, r, g, b),
            pos.getX() + 0.13d + (0.7d * random.nextDouble()),
            pos.getY() + 0.5d,
            pos.getZ() + 0.13d + (0.7d * random.nextDouble()),
            0d, 0d, 0d);
    }

    @Override
    public Class<Casks> typeForFeature() {
        return Casks.class;
    }

    static {
        X1 = Block.box(1.0d, 0.0d, 4.0d, 15.0d, 16.0d, 12.0d);
        X2 = Block.box(1.0d, 1.0d, 2.0d, 15.0d, 15.0d, 14.0d);
        X3 = Block.box(1.0d, 2.0d, 1.0d, 15.0d, 14.0d, 15.0d);
        X4 = Block.box(1.0d, 4.0d, 0.0d, 15.0d, 12.0d, 16.0d);
        Y1 = Block.box(4.0d, 1.0d, 0.0d, 12.0d, 15.0d, 16.0d);
        Y2 = Block.box(2.0d, 1.0d, 1.0d, 14.0d, 15.0d, 15.0d);
        Y3 = Block.box(1.0d, 1.0d, 2.0d, 15.0d, 15.0d, 14.0d);
        Y4 = Block.box(0.0d, 1.0d, 4.0d, 16.0d, 15.0d, 12.0d);
        Z1 = Block.box(4.0d, 0.0d, 1.0d, 12.0d, 16.0d, 15.0d);
        Z2 = Block.box(2.0d, 1.0d, 1.0d, 14.0d, 15.0d, 15.0d);
        Z3 = Block.box(1.0d, 2.0d, 1.0d, 15.0d, 14.0d, 15.0d);
        Z4 = Block.box(0.0d, 4.0d, 1.0d, 16.0d, 12.0d, 15.0d);
        X_SHAPE = Shapes.or(X1, X2, X3, X4);
        Y_SHAPE = Shapes.or(Y1, Y2, Y3, Y4);
        Z_SHAPE = Shapes.or(Z1, Z2, Z3, Z4);
    }

    public static class BlockItem extends net.minecraft.world.item.BlockItem {
        public BlockItem(Supplier<CaskBlock> block) {
            super(block.get(), new Properties());
        }
    }
}
