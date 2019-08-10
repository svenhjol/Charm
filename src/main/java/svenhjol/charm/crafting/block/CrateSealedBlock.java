package svenhjol.charm.crafting.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmSounds;
import svenhjol.charm.crafting.feature.Crate;
import svenhjol.meson.enums.WoodType;

public class CrateSealedBlock extends CrateBaseBlock
{
    public static final ResourceLocation CONTENTS = new ResourceLocation("contents");

    public CrateSealedBlock(WoodType wood)
    {
        super(wood);
        setRegistryName(new ResourceLocation(Charm.MOD_ID, "crate_sealed_" + wood.getName()));
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof IInventory) {
                InventoryHelper.dropInventoryItems(world, pos, (IInventory)tile);
                world.updateComparatorOutputLevel(pos, this);
                world.playSound(null, pos, CharmSounds.WOOD_SMASH, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
            }
        }
    }

    public static Block getBlockByWood(WoodType wood)
    {
        return Crate.sealedTypes.get(wood);
    }
}
