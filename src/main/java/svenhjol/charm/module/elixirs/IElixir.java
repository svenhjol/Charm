package svenhjol.charm.module.elixirs;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import svenhjol.charm.helper.PotionHelper;
import svenhjol.charm.helper.TextHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public interface IElixir {
    List<MobEffect> getValidStatusEffects();

    // in seconds
    int getMinDuration();

    // in seconds
    int getMaxDuration();

    int getMinAmplifier();

    int getMaxAmplifier();

    default Component getName() {
        int i = new Random().nextInt(16) + 1;
        Component word = TextHelper.translatable("item.charm.elixirs.adjective" + i);
        return TextHelper.translatable("item.charm.elixirs.itemname", word);
    }

    default List<MobEffectInstance> getEffects() {
        List<MobEffectInstance> instances = new ArrayList<>();
        List<MobEffect> statusEffects = getValidStatusEffects();
        Random random = new Random();

        int minDuration = getMinDuration();
        int maxDuration = getMaxDuration();

        int minAmplifier = getMinAmplifier();
        int maxAmplifier = getMaxAmplifier();

        statusEffects.forEach(effect -> {
            int durationBound = Math.max(1, maxDuration - minDuration);
            int amplifierBound = Math.max(1, maxAmplifier - minAmplifier);

            int duration = Math.max(1, random.nextInt(durationBound) + minDuration) * 20; // in ticks
            int amplifier = random.nextInt(amplifierBound) + minAmplifier;

            instances.add(new MobEffectInstance(effect, duration, amplifier));
        });

        return instances;
    }

    default ItemStack getPotionItem() {
        ItemStack bottle = PotionHelper.getFilledWaterBottle(1);

        // apply effects
        PotionUtils.setCustomEffects(bottle, getEffects());

        // apply custom name
        bottle.setHoverName(getName());

        return bottle;
    }
}
