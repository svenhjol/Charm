package svenhjol.charm.module.entity_spawners;

import net.minecraft.block.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.block.CharmBlockWithEntity;
import svenhjol.charm.module.entity_spawners.EntitySpawnerBlockEntity;
import svenhjol.charm.module.entity_spawners.EntitySpawners;

import javax.annotation.Nullable;

public class EntitySpawnerBlock extends CharmBlockWithEntity {
    public EntitySpawnerBlock(CharmModule module) {
        super(module, "entity_spawner", BlockBehaviour.Properties
            .of(Material.AIR)
            .noCollission()
            .noDrops());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new svenhjol.charm.module.entity_spawners.EntitySpawnerBlockEntity(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return world.isClientSide ? null : createTickerHelper(blockEntityType, EntitySpawners.BLOCK_ENTITY, EntitySpawnerBlockEntity::tick);
    }

    @Override
    public void createBlockItem(ResourceLocation id) {
        // don't
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> list) {
        // don't
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }
}
