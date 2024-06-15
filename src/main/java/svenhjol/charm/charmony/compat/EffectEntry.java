package svenhjol.charm.charmony.compat;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

public record EffectEntry(MobEffect effect, int duration) {
    public EffectEntry(MobEffect effect, int duration) {
        this.effect = effect;
        this.duration = duration;
    }

    public MobEffectInstance createEffectInstance() {
        return new MobEffectInstance(this.effect, this.duration);
    }

    public MobEffect effect() {
        return this.effect;
    }

    public int duration() {
        return this.duration;
    }
}
