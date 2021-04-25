package svenhjol.charm.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
    private static final String BLOCK_ENTITY_TAG = "BlockEntityTag";

    public AstrolabeBlock(CharmModule module) {
        super(module, "astrolabe", Settings.copy(Blocks.COPPER_BLOCK));
    }

    @Override
    public void createBlockItem(Identifier id) {
        AstrolabeBlockItem blockItem = new AstrolabeBlockItem(this);
        RegistryHandler.item(id, blockItem);
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
}
