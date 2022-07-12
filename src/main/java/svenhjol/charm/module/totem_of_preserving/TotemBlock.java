package svenhjol.charm.module.totem_of_preserving;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
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
import svenhjol.charm.block.CharmBlockWithEntity;
import svenhjol.charm.helper.PlayerHelper;
import svenhjol.charm.loader.CharmModule;

public class TotemBlock extends CharmBlockWithEntity {
    public static final VoxelShape SHAPE = Block.box(2, 2, 2, 14, 14, 14);

    public TotemBlock(CharmModule module) {
        super(module, "totem_of_preserving_holder", Properties.copy(Blocks.GLASS)
            .strength(-1.0f, 3600000.0f)
            .isValidSpawn((a, b, c, d) -> false)
            .noOcclusion()
            .noDrops());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TotemBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public VoxelShape getVisualShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.empty();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> list) {
        // no
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof Player player
            && !player.getLevel().isClientSide()
            && player.isAlive()
            && level.getBlockEntity(pos) instanceof TotemBlockEntity totem
            && (!TotemOfPreserving.ownerOnly || totem.getOwner().equals(player.getUUID()))
        ) {
            // Create a new totem item and give it to player.
            var totemItem = new ItemStack(TotemOfPreserving.ITEM);
            TotemOfPreservingItem.setItems(totemItem, totem.getItems());
            TotemOfPreservingItem.setMessage(totemItem, totem.getMessage());
            TotemOfPreservingItem.setXp(totemItem, totem.getXp());
            PlayerHelper.addOrDropStack(player, totemItem);

            // Remove the totem block.
            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        }

        super.entityInside(state, level, pos, entity);
    }
}
