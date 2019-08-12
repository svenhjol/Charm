package svenhjol.charm.tweaks.feature;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.meson.Feature;

public class TamedAnimalsNoDamage extends Feature
{
    @SubscribeEvent
    public void onDamage(LivingHurtEvent event)
    {
        if (!event.isCanceled()
            && (!(event.getEntityLiving() instanceof PlayerEntity))
        ) {
            Entity attacker = event.getSource().getImmediateSource();
            Entity source = event.getSource().getTrueSource();

            if (source instanceof PlayerEntity || attacker instanceof PlayerEntity) {
                LivingEntity target = event.getEntityLiving();
                if (target instanceof TameableEntity
                    && ((TameableEntity)target).isTamed()
                ) {
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
