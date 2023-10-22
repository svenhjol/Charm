package svenhjol.charm.feature.totem_of_preserving;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import svenhjol.charmony.base.CharmonyBlockWithEntity;
import svenhjol.charmony.common.CommonFeature;

@SuppressWarnings("deprecation")
public class TotemBlock extends CharmonyBlockWithEntity {
    static final VoxelShape SHAPE = Block.box(2, 2, 2, 14, 14, 14);

    public TotemBlock(CommonFeature feature) {
        super(feature, Properties.copy(Blocks.GLASS)
            .strength(-1.0f, 3600000.0f)
            .noCollission()
            .noOcclusion()
            .noLootTable());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TotemBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.empty();
    }

    @Override
    public VoxelShape getVisualShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.empty();
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    /**
     * Executed when the game wants to delete a totem block.
     */
    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos pos, BlockState state, boolean bl) {
        var log = feature.mod().log();
        var dimension = level.dimension().location();

        if (TotemOfPreserving.PROTECT_POSITIONS.containsKey(dimension)
            && TotemOfPreserving.PROTECT_POSITIONS.get(dimension).contains(pos)
            && level.getBlockEntity(pos) instanceof TotemBlockEntity totem
            && !level.isClientSide) {

            log.debug(getClass(), "Something wants to overwrite the totem block, emergency item drop");
            var items = totem.getItems();
            for (ItemStack stack : items) {
                var itemEntity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), stack);
                level.addFreshEntity(itemEntity);
            }
        }
        log.debug(getClass(), "Going to remove a totem block");
        super.onRemove(blockState, level, pos, state, bl);
    }

    /**
     * Executed when an entity is within the block bounds.
     */
    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof Player player
            && !player.level().isClientSide()
            && player.isAlive()
            && level.getBlockEntity(pos) instanceof TotemBlockEntity totem
            && (!TotemOfPreserving.ownerOnly
                || (totem.getOwner().equals(player.getUUID()) || player.getAbilities().instabuild))
        ) {
            var log = feature.mod().log();
            var serverLevel = (ServerLevel)level;
            var dimension = serverLevel.dimension().location();

            // Create a new totem item and give it to player.
            log.debug(getClass(), "Player has interacted with totem holder block at pos: " + pos + ", player: " + player);
            var totemItem = new ItemStack(TotemOfPreserving.item.get());

            TotemItem.setItems(totemItem, totem.getItems());
            TotemItem.setMessage(totemItem, totem.getMessage());
            TotemItem.setTotemDamage(totemItem, totem.getDamage());
            TotemItem.setGlint(totemItem, true);

            log.debug(getClass(), "Adding totem item to player's inventory: " + player);
            player.getInventory().add(totemItem);

            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.6f, 1.0f);

            if (TotemOfPreserving.PROTECT_POSITIONS.containsKey(dimension)) {
                TotemOfPreserving.PROTECT_POSITIONS.get(dimension).remove(pos);
            }

            // Remove the totem block.
            log.debug(getClass(), "Removing totem holder block and block entity: " + pos);
            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        }

        super.entityInside(state, level, pos, entity);
    }
}
