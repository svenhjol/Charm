package svenhjol.charm.decoration.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import svenhjol.charm.base.message.MessageCrateInteract;
import svenhjol.charm.decoration.module.Crates;
import svenhjol.meson.MesonModule;
import svenhjol.meson.enums.WoodType;
import svenhjol.meson.handler.PacketHandler;

public class CrateSealedBlock extends CrateBaseBlock
{
    public CrateSealedBlock(MesonModule module, WoodType wood)
    {
        super(module, "crate_sealed_" + wood.getName(), wood);
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof IInventory) {
                InventoryHelper.dropInventoryItems(world, pos, (IInventory)tile);
                world.updateComparatorOutputLevel(pos, this);
                PacketHandler.sendToAll(new MessageCrateInteract(pos, 2));
            }
        }
    }

    public static Block getBlockByWood(WoodType wood)
    {
        return Crates.sealedTypes.get(wood);
    }
}
