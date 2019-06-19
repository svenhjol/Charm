package svenhjol.charm.tweaks.feature;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import svenhjol.meson.Feature;
import svenhjol.meson.helper.SoundHelper;

public class TamedAnimalsNoDamage extends Feature
{
    @Override
    public String getDescription()
    {
        return "Tamed animals no longer take direct damage from players.\n" +
                "They still suffer secondary effects, such as burning from a Fire Aspect sword.";
    }

    @SubscribeEvent
    public void onDamage(LivingHurtEvent event)
    {
        if (!event.isCanceled()
            && (!(event.getEntityLiving() instanceof EntityPlayer))
        ) {
            Entity attacker = event.getSource().getImmediateSource();

            if (attacker instanceof EntityPlayer) {
                EntityLivingBase target = event.getEntityLiving();
                if (target instanceof EntityTameable && ((EntityTameable) target).isTamed()) {
                    event.setCanceled(true);
                    if (attacker.world.isRemote) {
                        SoundHelper.playerSound((EntityPlayer) attacker, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 0.8f, SoundCategory.NEUTRAL);
                    }
                }

            }
        }
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
