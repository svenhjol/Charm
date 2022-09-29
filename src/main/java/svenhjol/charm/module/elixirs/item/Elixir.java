package svenhjol.charm.module.elixirs.item;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import svenhjol.charm.module.elixirs.IElixir;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Elixir implements IElixir {
    @Override
    public List<MobEffect> getValidStatusEffects() {
        List<MobEffect> possibleEffects = new ArrayList<>(Arrays.asList(
            MobEffects.MOVEMENT_SPEED,
            MobEffects.DIG_SPEED,
            MobEffects.DAMAGE_BOOST,
            MobEffects.JUMP,
            MobEffects.DAMAGE_RESISTANCE,
            MobEffects.HEALTH_BOOST,
            MobEffects.ABSORPTION,
            MobEffects.SATURATION,
            MobEffects.LUCK,
            MobEffects.DOLPHINS_GRACE,
            MobEffects.REGENERATION,
            MobEffects.NIGHT_VISION,
            MobEffects.INVISIBILITY,
            MobEffects.WATER_BREATHING,
            MobEffects.FIRE_RESISTANCE
        ));

        List<MobEffect> selectedEffects = new ArrayList<>();
        selectedEffects.add(possibleEffects.get(new Random().nextInt(possibleEffects.size())));
        return selectedEffects;
    }

    @Override
    public int getMinDuration() {
        return 240;
    }

    @Override
    public int getMaxDuration() {
        return 480;
    }

    @Override
    public int getMinAmplifier() {
        return 2;
    }

    @Override
    public int getMaxAmplifier() {
        return 5;
    }
}
