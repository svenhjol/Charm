package svenhjol.charm.world.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmSounds;
import svenhjol.charm.world.entity.EntityEndermitePowder;
import svenhjol.meson.MesonItem;
import svenhjol.meson.helper.WorldHelper;

public class ItemEndermitePowder extends MesonItem
{
    public ItemEndermitePowder()
    {
        super("endermite_powder");
        setCreativeTab(CreativeTabs.MISC);
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack stack = playerIn.getHeldItem(handIn);

        if (worldIn.provider.getDimension() != 1) {
            return new ActionResult<>(EnumActionResult.FAIL, stack);
        }

        if (!playerIn.capabilities.isCreativeMode) {
            stack.shrink(1);
        }

        // client
        if (worldIn.isRemote) {
            playerIn.swingArm(handIn);
        }

        // server
        if (!worldIn.isRemote) {
            BlockPos pos = WorldHelper.getNearestStructure(worldIn, playerIn.getPosition(), "EndCity");
            if (pos != null) {
                EntityEndermitePowder entity = new EntityEndermitePowder(worldIn, pos.getX(), pos.getZ());
                Vec3d look = playerIn.getLookVec();
                entity.setPosition(playerIn.posX + look.x * 2, playerIn.posY + 0.5, playerIn.posZ + look.z * 2);
                worldIn.spawnEntity(entity);
                worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, CharmSounds.ENDER_WHISPERS, SoundCategory.PLAYERS, 1F, 1F);
            }
        }

        StatBase stats = StatList.getObjectUseStats(this);
        if (stats != null) {
            playerIn.addStat(stats);
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }
}
