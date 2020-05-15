package svenhjol.charm.decoration.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import svenhjol.charm.decoration.module.Crates;
import svenhjol.charm.decoration.tileentity.CrateTileEntity;
import svenhjol.meson.MesonModule;
import svenhjol.meson.enums.IWoodType;

import java.util.List;

@SuppressWarnings("deprecation")
public class CrateOpenBlock extends CrateBaseBlock {
    public static final ResourceLocation CONTENTS = new ResourceLocation("contents");

    public CrateOpenBlock(MesonModule module, IWoodType wood) {
        super(module, "crate_open_" + wood.getName(), wood);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (world.isRemote) {
            return ActionResultType.PASS;
        } else if (player.isSpectator()) {
            return ActionResultType.PASS;
        } else {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof CrateTileEntity) {
                CrateTileEntity crate = (CrateTileEntity) tile;
                crate.fillWithLoot(player);
                player.openContainer(crate);
                /* @todo stats, see ShulkerBoxBlock */
                return ActionResultType.SUCCESS;
            }
            return ActionResultType.FAIL;
        }
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof CrateTileEntity) {
            CrateTileEntity crate = (CrateTileEntity) tile;
            if (!world.isRemote && player.isCreative()) {
                ItemStack stack = new ItemStack(getBlockByWood(this.wood));
                CompoundNBT tag = crate.write(new CompoundNBT());
                if (!tag.isEmpty()) {
                    stack.setTagInfo("BlockEntityTag", tag);
                }
                if (crate.hasCustomName()) {
                    stack.setDisplayName(crate.getCustomName());
                }
                ItemEntity entity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack);
                entity.setDefaultPickupDelay();
                world.addEntity(entity);
            } else {
                crate.fillWithLoot(player);
            }
        }
        super.onBlockHarvested(world, pos, state, player);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        TileEntity tile = builder.get(LootParameters.BLOCK_ENTITY);
        if (tile instanceof CrateTileEntity) {
            CrateTileEntity crate = (CrateTileEntity) tile;
            builder = builder.withDynamicDrop(CONTENTS, (context, consumer) -> {
                for (int i = 0; i < crate.getSizeInventory(); i++) {
                    consumer.accept(crate.getStackInSlot(i));
                }
            });
        }
        return super.getDrops(state, builder);
    }

    public static Block getBlockByWood(IWoodType wood) {
        return Crates.openTypes.get(wood);
    }
}
