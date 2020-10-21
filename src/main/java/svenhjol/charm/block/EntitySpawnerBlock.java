package svenhjol.charm.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.block.CharmBlockWithEntity;
import svenhjol.charm.blockentity.EntitySpawnerBlockEntity;

import javax.annotation.Nullable;

public class EntitySpawnerBlock extends CharmBlockWithEntity {
    public EntitySpawnerBlock(CharmModule module) {
        super(module, "entity_spawner", AbstractBlock.Settings
            .of(Material.AIR)
            .noCollision()
            .dropsNothing());
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new EntitySpawnerBlockEntity();
    }

    @Override
    public void createBlockItem(Identifier id) {
        // don't
    }

    @Override
    public void addStacksForDisplay(ItemGroup group, DefaultedList<ItemStack> list) {
        // don't
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }
}
