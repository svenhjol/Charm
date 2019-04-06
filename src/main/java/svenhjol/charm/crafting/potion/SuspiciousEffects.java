package svenhjol.charm.crafting.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import svenhjol.charm.crafting.feature.SuspiciousSoup;

public class SuspiciousEffects
{
    public static class CustomEffect
    {
        public void performEffect(EntityLivingBase e, int duration, int amplifier)
        {
            // no op
        }

        public void doEffect(EntityLivingBase e, Potion potion, int duration, int amplifier)
        {
            e.addPotionEffect(new PotionEffect(potion, duration, amplifier));
        }

        public void doSideEffect(EntityLivingBase e, Potion potion, float chance, int duration, int amplifier)
        {
            if (e != null && e.world.rand.nextFloat() <= chance) {
                doEffect(e, potion, duration, amplifier);
            }
        }
    }

    public static void init()
    {
        SuspiciousSoup.effects.add(new CustomEffect() {
            @Override
            public void performEffect(EntityLivingBase e, int duration, int amplifier)
            {
                doEffect(e, MobEffects.NIGHT_VISION, duration, amplifier);
                doSideEffect(e, MobEffects.WEAKNESS, 0.5f, duration, amplifier);
            }
        });

        SuspiciousSoup.effects.add(new CustomEffect() {
            @Override
            public void performEffect(EntityLivingBase e, int duration, int amplifier)
            {
                doEffect(e, MobEffects.SPEED, duration, amplifier);
                doSideEffect(e, MobEffects.HUNGER, 0.5f, duration, amplifier);
            }
        });

        SuspiciousSoup.effects.add(new CustomEffect() {
            @Override
            public void performEffect(EntityLivingBase e, int duration, int amplifier)
            {
                doEffect(e, MobEffects.SPEED, duration, amplifier * 2);
                doSideEffect(e, MobEffects.POISON, 0.5f, duration, amplifier);
            }
        });

        SuspiciousSoup.effects.add(new CustomEffect() {
            @Override
            public void performEffect(EntityLivingBase e, int duration, int amplifier)
            {
                doEffect(e, MobEffects.REGENERATION, duration, amplifier);
                doSideEffect(e, MobEffects.SLOWNESS, 0.5f, duration, amplifier);
            }
        });

        SuspiciousSoup.effects.add(new CustomEffect() {
            @Override
            public void performEffect(EntityLivingBase e, int duration, int amplifier)
            {
                doEffect(e, MobEffects.STRENGTH, duration, amplifier);
                doSideEffect(e, MobEffects.HUNGER, 0.5f, duration, amplifier);
            }
        });

        SuspiciousSoup.effects.add(new CustomEffect() {
            @Override
            public void performEffect(EntityLivingBase e, int duration, int amplifier)
            {
                doEffect(e, MobEffects.STRENGTH, duration, amplifier * 2);
                doSideEffect(e, MobEffects.POISON, 0.5f, duration, amplifier);
            }
        });

        SuspiciousSoup.effects.add(new CustomEffect() {
            @Override
            public void performEffect(EntityLivingBase e, int duration, int amplifier)
            {
                doEffect(e, MobEffects.JUMP_BOOST, duration, amplifier * 2);
                doSideEffect(e, MobEffects.HUNGER, 0.5f, duration, amplifier);
            }
        });

        SuspiciousSoup.effects.add(new CustomEffect() {
            @Override
            public void performEffect(EntityLivingBase e, int duration, int amplifier)
            {
                doEffect(e, MobEffects.RESISTANCE, duration, amplifier);
                doSideEffect(e, MobEffects.SLOWNESS, 0.5f, duration, amplifier);
            }
        });

        SuspiciousSoup.effects.add(new CustomEffect() {
            @Override
            public void performEffect(EntityLivingBase e, int duration, int amplifier)
            {
                int d = duration * (e.world.rand.nextInt(3) + 1);
                doEffect(e, MobEffects.WATER_BREATHING, d, amplifier);
                doSideEffect(e, MobEffects.SLOWNESS, 0.5f, d, amplifier);
            }
        });

        SuspiciousSoup.effects.add(new CustomEffect() {
            @Override
            public void performEffect(EntityLivingBase e, int duration, int amplifier)
            {
                int d = duration * (e.world.rand.nextInt(3) + 1);
                doEffect(e, MobEffects.HASTE, d, amplifier);
                doSideEffect(e, MobEffects.BLINDNESS, 0.5f, d, amplifier);
            }
        });

        SuspiciousSoup.effects.add(new CustomEffect() {
            @Override
            public void performEffect(EntityLivingBase e, int duration, int amplifier)
            {
                int d = duration * (e.world.rand.nextInt(3) + 1);
                doEffect(e, MobEffects.FIRE_RESISTANCE, d, amplifier);
                doSideEffect(e, MobEffects.SLOWNESS, 0.5f, d, amplifier);
            }
        });

        SuspiciousSoup.effects.add(new CustomEffect() {
            @Override
            public void performEffect(EntityLivingBase e, int duration, int amplifier)
            {
                int d = duration * (e.world.rand.nextInt(3) + 1);
                doEffect(e, MobEffects.HEALTH_BOOST, d, amplifier);
                doSideEffect(e, MobEffects.HUNGER, 0.5f, d, amplifier);
            }
        });

        SuspiciousSoup.effects.add(new CustomEffect() {
            @Override
            public void performEffect(EntityLivingBase e, int duration, int amplifier)
            {
                int d = duration * (e.world.rand.nextInt(3) + 1);
                doEffect(e, MobEffects.INVISIBILITY, d, amplifier);
                doSideEffect(e, MobEffects.HUNGER, 0.5f, d, amplifier * 2);
            }
        });
    }
}