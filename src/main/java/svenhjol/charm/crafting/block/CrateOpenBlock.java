package svenhjol.charm.crafting.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import svenhjol.charm.Charm;
import svenhjol.charm.crafting.feature.Crate;
import svenhjol.charm.crafting.tileentity.CrateTileEntity;
import svenhjol.meson.enums.WoodType;

import java.util.List;

public class CrateOpenBlock extends CrateBaseBlock
{
    public static final ResourceLocation CONTENTS = new ResourceLocation("contents");

    public CrateOpenBlock(WoodType wood)
    {
        super(wood);
        setRegistryName(new ResourceLocation(Charm.MOD_ID, "crate_open_" + wood.getName()));
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        if (world.isRemote) {
            return true;
        } else if (player.isSpectator()) {
            return true;
        } else {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof CrateTileEntity) {
                CrateTileEntity crate = (CrateTileEntity)tile;
                player.openContainer(crate);
                /* @todo stats, see ShulkerBoxBlock */
                return true;
            }
            return false;
        }
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player)
    {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof CrateTileEntity) {
            CrateTileEntity crate = (CrateTileEntity)tile;
            if (!world.isRemote && player.isCreative()) {
                ItemStack stack = new ItemStack(getBlockByWood(getWood()));
                CompoundNBT tag = crate.writeToNBT(new CompoundNBT()); /* @todo might need separate save method */
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
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder)
    {
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

    public static Block getBlockByWood(WoodType wood)
    {
        return Crate.openTypes.get(wood);
    }
}
