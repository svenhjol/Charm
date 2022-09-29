package svenhjol.charm.module.elixirs.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import svenhjol.charm.helper.TextHelper;
import svenhjol.charm.module.elixirs.IElixir;

import java.util.Arrays;
import java.util.List;

public class Moonshine implements IElixir {
    @Override
    public List<MobEffect> getValidStatusEffects() {
        return Arrays.asList(
            MobEffects.REGENERATION,
            MobEffects.HEALTH_BOOST
        );
    }

    @Override
    public List<MobEffectInstance> getEffects() {
        List<MobEffectInstance> effects = IElixir.super.getEffects();
        effects.add(new MobEffectInstance(MobEffects.BLINDNESS, (getMinDuration() / 4) * 20, 0));
        effects.add(new MobEffectInstance(MobEffects.CONFUSION, (getMinDuration() / 4) * 20, 0));
        return effects;
    }

    @Override
    public Component getName() {
        return TextHelper.translatable("item.charm.elixirs.moonshine");
    }

    @Override
    public int getMinDuration() {
        return 100;
    }

    @Override
    public int getMaxDuration() {
        return 200;
    }

    @Override
    public int getMinAmplifier() {
        return 3;
    }

    @Override
    public int getMaxAmplifier() {
        return 5;
    }
}
