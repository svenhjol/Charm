package svenhjol.charm.tweaks.feature;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import svenhjol.meson.Feature;

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
            Entity trueSource = event.getSource().getTrueSource();

            if (attacker instanceof EntityPlayer || trueSource instanceof EntityPlayer) {
                EntityLivingBase target = event.getEntityLiving();
                if (target instanceof EntityTameable && ((EntityTameable) target).isTamed()) {
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
}
