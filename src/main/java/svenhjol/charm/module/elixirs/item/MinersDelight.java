package svenhjol.charm.module.elixirs.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import svenhjol.charm.Charm;
import svenhjol.charm.helper.TextHelper;
import svenhjol.charm.module.bat_buckets.BatBuckets;
import svenhjol.charm.module.elixirs.IElixir;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MinersDelight implements IElixir {
    @Override
    public List<MobEffect> getValidStatusEffects() {
        var effects = new ArrayList<>(Arrays.asList(
            MobEffects.NIGHT_VISION,
            MobEffects.DIG_SPEED
        ));

        if (Charm.LOADER.isEnabled(BatBuckets.class)) {
            effects.add(BatBuckets.ECHOLOCATION);
        }

        return effects;
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
