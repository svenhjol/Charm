package svenhjol.charm.tweaks.feature;

import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import svenhjol.meson.Feature;

import java.util.List;

public class TamedAnimalsHealing extends Feature
{
    @Override
    public String getDescription()
    {
        return "Tame animals will heal within the range of a beacon with regeneration effect.";
    }

    public static void heal(World world, AxisAlignedBB aabb, Potion primaryEffect, Potion secondaryEffect, int duration, int amplifier)
    {
        // one of the beacon effects must be regeneration
        if (primaryEffect != MobEffects.REGENERATION && secondaryEffect != MobEffects.REGENERATION) return;

        List<EntityTameable> list = world.getEntitiesWithinAABB(EntityTameable.class, aabb);

        for (EntityTameable animal : list)
        {
            if (!animal.isTamed()) continue;
            animal.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, duration, amplifier, true, true));
        }
    }
}
