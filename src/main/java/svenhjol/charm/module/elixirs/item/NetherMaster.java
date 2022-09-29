package svenhjol.charm.module.elixirs.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import svenhjol.charm.helper.TextHelper;
import svenhjol.charm.module.elixirs.IElixir;

import java.util.Arrays;
import java.util.List;

public class NetherMaster implements IElixir {
    @Override
    public List<MobEffect> getValidStatusEffects() {
        return Arrays.asList(
            MobEffects.FIRE_RESISTANCE,
            MobEffects.NIGHT_VISION,
            MobEffects.REGENERATION
        );
    }

    @Override
    public Component getName() {
        return TextHelper.translatable("item.charm.elixirs.nether_master");
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
        return 1;
    }

    @Override
    public int getMaxAmplifier() {
        return 1;
    }
}
