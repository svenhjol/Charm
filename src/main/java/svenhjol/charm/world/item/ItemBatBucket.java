package svenhjol.charm.world.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.world.feature.BatBucket;
import svenhjol.charm.world.message.MessageGlowing;
import svenhjol.meson.iface.IMesonItem;
import svenhjol.meson.handler.NetworkHandler;
import svenhjol.meson.helper.EntityHelper;
import svenhjol.meson.helper.SoundHelper;

public class ItemBatBucket extends Item implements IMesonItem
{
    public int max;
    public double range;

    public ItemBatBucket()
    {
        register("bat_bucket");
        setMaxStackSize(1);
        setCreativeTab(CreativeTabs.MISC);
        this.max = BatBucket.maxSeconds * 20;
        this.range = (double)BatBucket.range;
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        SoundHelper.playerSound(player, SoundEvents.ENTITY_BAT_TAKEOFF, 1.0f, 1.0f);

        ItemStack stack = player.getHeldItem(hand);
        stack.shrink(1);
        if (!worldIn.isRemote) {
            EntityHelper.spawnEntityNearPlayer(player, 2, new ResourceLocation("bat"));
            NetworkHandler.INSTANCE.sendTo(new MessageGlowing(max, range), (EntityPlayerMP) player);
        }
        player.inventory.addItemStackToInventory(new ItemStack(Items.BUCKET));

        // stat handling, taken from base MC
        StatBase objectUseStats = StatList.getObjectUseStats(this);
        if (objectUseStats != null) {
            player.addStat(objectUseStats);
        }

        return EnumActionResult.PASS;
    }

}
