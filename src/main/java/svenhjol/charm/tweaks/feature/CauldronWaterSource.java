package svenhjol.charm.tweaks.feature;

import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import svenhjol.meson.Feature;
import svenhjol.meson.helper.ItemHelper;
import svenhjol.meson.helper.PlayerHelper;

public class CauldronWaterSource extends Feature
{
    @Override
    public String getDescription()
    {
        return "Cauldrons can be used as a permanent water source when sneaking.";
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
        if (event.getWorld().getBlockState(event.getPos()).getBlock() == Blocks.CAULDRON && event.getEntityPlayer() != null) {
            EntityPlayer player = event.getEntityPlayer();
            ItemStack held = player.getHeldItem(event.getHand());
            IBlockState state = event.getWorld().getBlockState(event.getPos());

            if (event.getEntityPlayer().isSneaking()) {

                if (isFilledCauldron(state)) {
                    ItemStack itemToSet = null;
                    if (held.getItem() == Items.GLASS_BOTTLE) {
                        itemToSet = ItemHelper.getFilledWaterBottle();
                    } else if (held.getItem() == Items.BUCKET) {
                        itemToSet = new ItemStack(Items.WATER_BUCKET);
                    }

                    if (itemToSet != null) {
                        event.setResult(Event.Result.DENY);
                        PlayerHelper.setHeldItem(player, event.getHand(), itemToSet);
                    }
                }
            } else {

                if (held.getItem() == Items.POTIONITEM
                    && PotionUtils.getPotionFromItem(held) == PotionTypes.WATER
                    && held.getCount() > 1
                ) {
                    event.setResult(Event.Result.DENY);
                    event.setCanceled(true);
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
