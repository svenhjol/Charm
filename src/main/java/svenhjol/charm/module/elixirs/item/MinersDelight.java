package svenhjol.charm.module.elixirs.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import svenhjol.charm.helper.TextHelper;
import svenhjol.charm.module.elixirs.IElixir;

import java.util.Arrays;
import java.util.List;

public class MinersDelight implements IElixir {
    @Override
    public List<MobEffect> getValidStatusEffects() {
        return Arrays.asList(
            MobEffects.NIGHT_VISION,
            MobEffects.DIG_SPEED
        );
    }

    @Override
    public Component getName() {
        return TextHelper.translatable("item.charm.elixirs.miners_delight");
    }

    @Override
    public int getMinDuration() {
        return 480;
    }

    @Override
    public int getMaxDuration() {
        return 960;
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
