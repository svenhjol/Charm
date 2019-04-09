package svenhjol.charm.misc.feature;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import svenhjol.meson.Feature;

public class UseTotemFromInventory extends Feature
{
    @Override
    public String getDescription()
    {
        return "As long as a Totem of Undying is in your inventory, it will be consumed to protect you from death.";
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event)
    {
        if (!event.isCanceled()
            && event.getEntityLiving() instanceof EntityPlayer
        ) {

            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            InventoryPlayer inventory = player.inventory;
            ItemStack totem = new ItemStack(Items.TOTEM_OF_UNDYING);

            // check inventory for a totem
            if (inventory.hasItemStack(totem)) {
                int slot = inventory.findSlotMatchingUnusedItem(totem);
                inventory.removeStackFromSlot(slot);

                // do the achievement stuff.  From base MC
                if (player instanceof EntityPlayerMP) {
                    EntityPlayerMP entityplayermp = (EntityPlayerMP) player;
                    StatBase objectUseStats = StatList.getObjectUseStats(Items.TOTEM_OF_UNDYING);
                    if (objectUseStats != null) {
                        entityplayermp.addStat(objectUseStats);
                    }
                    CriteriaTriggers.USED_TOTEM.trigger(entityplayermp, totem);
                }

                // stolen from EntityLivingBase::checkTotemDeathProtection()
                player.setHealth(1.0F);
                player.clearActivePotions();
                player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 900, 1));
                player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 100, 1));
                player.world.setEntityState(player, (byte) 35);

                // don't actually die
                event.setCanceled(true);
            }
        }
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}