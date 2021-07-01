package svenhjol.charm.potion;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import svenhjol.charm.loader.CommonModule;

public abstract class CharmStatusEffect extends MobEffect implements ICharmStatusEffect {
    protected CommonModule module;

    protected CharmStatusEffect(CommonModule module, String name, MobEffectCategory type, int color) {
        super(type, color);
        this.module = module;
        this.register(module, name);
    }

    protected void addStatusEffect(MobEffect effect) {
        effect.getAttributeModifiers().forEach((attribute, modifier) -> {
            this.addAttributeModifier(attribute, modifier.getId().toString(), modifier.getAmount(), modifier.getOperation());
        });
    }
}
