package svenhjol.charm.tweaks.module;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.CauldronHelper;
import svenhjol.meson.helper.PlayerHelper;
import svenhjol.meson.helper.PotionHelper;
import svenhjol.meson.iface.Module;

@Module(mod = Charm.MOD_ID, category = CharmCategories.TWEAKS, hasSubscriptions = true,
    description = "Cauldrons can be used as a permanent water source when sneaking.")
public class CauldronWaterSource extends MesonModule {
    @SubscribeEvent
    public void onBucketUse(FillBucketEvent event) {
        if (event.getEntityLiving() != null
            && event.getEntityLiving() instanceof PlayerEntity
            && PlayerHelper.isCrouching((PlayerEntity) event.getEntityLiving())
        ) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();

            if (event.getTarget() != null) {
                BlockState state = player.getEntityWorld().getBlockState(new BlockPos(event.getTarget().getHitVec()));
                if (CauldronHelper.isFull(state)) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public void onCauldronUse(PlayerInteractEvent.RightClickBlock event) {
        if (event.getWorld().getBlockState(event.getPos()).getBlock() == Blocks.CAULDRON && event.getPlayer() != null) {
            PlayerEntity player = event.getPlayer();
            World world = player.world;
            ItemStack held = player.getHeldItem(event.getHand());
            BlockState state = event.getWorld().getBlockState(event.getPos());

            if (PlayerHelper.isCrouching(player)) {
                ItemStack item = null;
                SoundEvent sound = null;

                if (CauldronHelper.hasWater(state) && held.getItem() == Items.GLASS_BOTTLE) {
                    item = PotionHelper.getFilledWaterBottle();
                    sound = SoundEvents.ITEM_BOTTLE_FILL;
                } else if (CauldronHelper.isFull(state) && held.getItem() == Items.BUCKET) {
                    item = new ItemStack(Items.WATER_BUCKET);
                    sound = SoundEvents.ITEM_BUCKET_FILL;
                }

                if (item != null) {
                    event.setResult(Event.Result.DENY);
                    PlayerHelper.setHeldItem(player, event.getHand(), item);
                    if (sound != null) {
                        world.playSound(null, player.getPosition(), sound, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    }
                }

            } else {

                // don't activate cauldron if stack contains multiple water bottles
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
}
