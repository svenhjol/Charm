package svenhjol.charm.misc.feature;

import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import svenhjol.meson.helper.ItemHelper;
import svenhjol.meson.helper.PlayerHelper;
import svenhjol.meson.Feature;

public class CauldronWaterSource extends Feature
{
    @Override
    public String getDescription()
    {
        return "Cauldrons can be used as a permanent water source when holding down shift.";
    }

    @SubscribeEvent
    public void onBucketUse(FillBucketEvent event)
    {
        if (event.getEntityLiving() != null
            && event.getEntityLiving() instanceof EntityPlayer
            && event.getEntityLiving().isSneaking()
        ) {
            EntityPlayer player = (EntityPlayer)event.getEntityLiving();

            if (event.getTarget() != null) {
                IBlockState state = player.getEntityWorld().getBlockState(event.getTarget().getBlockPos());
                if (isFilledCauldron(state)) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public void onCauldronUse(PlayerInteractEvent.RightClickBlock event)
    {
        if (!event.getWorld().isRemote
            && event.getWorld().getBlockState(event.getPos()).getBlock() == Blocks.CAULDRON
            && event.getEntityPlayer() != null
            && event.getEntityPlayer().isSneaking()
        ) {
            EntityPlayer player = event.getEntityPlayer();
            IBlockState state = event.getWorld().getBlockState(event.getPos());
            Item item = player.getHeldItem(event.getHand()).getItem();

            if (isFilledCauldron(state)) {
                ItemStack itemToSet = null;
                if (item == Items.GLASS_BOTTLE) {
                    itemToSet = ItemHelper.getFilledWaterBottle();
                } else if (item == Items.BUCKET) {
                    itemToSet = new ItemStack(Items.WATER_BUCKET);
                }

                if (itemToSet != null) {
                    event.setResult(Event.Result.DENY);
                    PlayerHelper.setHeldItem(player, event.getHand(), itemToSet);
                }
            }
        }
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }

    private boolean isFilledCauldron(IBlockState state)
    {
        return state.getProperties().containsKey(BlockCauldron.LEVEL) && state.getValue(BlockCauldron.LEVEL) == 3;
    }
}
