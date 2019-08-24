package svenhjol.charm.tweaks.module;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.PlayerHelper;
import svenhjol.meson.helper.PotionHelper;
import svenhjol.meson.iface.Module;

@Module(mod = Charm.MOD_ID, category = CharmCategories.TWEAKS, hasSubscriptions = true,
    description = "Cauldrons can be used as a permanent water source when sneaking.")
public class CauldronWaterSource extends MesonModule
{
    @SubscribeEvent
    public void onBucketUse(FillBucketEvent event)
    {
        if (event.getEntityLiving() != null
            && event.getEntityLiving() instanceof PlayerEntity
            && event.getEntityLiving().isSneaking()
        ) {
            PlayerEntity player = (PlayerEntity)event.getEntityLiving();

            if (event.getTarget() != null) {
                BlockState state = player.getEntityWorld().getBlockState( new BlockPos(event.getTarget().getHitVec()) );
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
            PlayerEntity player = event.getEntityPlayer();
            ItemStack held = player.getHeldItem(event.getHand());
            BlockState state = event.getWorld().getBlockState(event.getPos());

            if (event.getEntityPlayer().isSneaking()) {

                if (isFilledCauldron(state)) {
                    ItemStack itemToSet = null;
                    if (held.getItem() == Items.GLASS_BOTTLE) {
                        itemToSet = PotionHelper.getFilledWaterBottle();
                    } else if (held.getItem() == Items.BUCKET) {
                        itemToSet = new ItemStack(Items.WATER_BUCKET);
                    }

                    if (itemToSet != null) {
                        event.setResult(Event.Result.DENY);
                        PlayerHelper.setHeldItem(player, event.getHand(), itemToSet);
                    }
                }
            } else {

                if (held.getItem() == Items.POTION
                    && PotionUtils.getPotionFromItem(held) == Potions.WATER
                    && held.getCount() > 1
                ) {
                    event.setResult(Event.Result.DENY);
                    event.setCanceled(true);
                }
            }
        }
    }

    private boolean isFilledCauldron(BlockState state)
    {
        return state.getProperties().contains(CauldronBlock.LEVEL) && state.get(CauldronBlock.LEVEL) == 3;
    }
}
