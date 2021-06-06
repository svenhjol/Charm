package svenhjol.charm.potion;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.potion.ICharmStatusEffect;

public abstract class CharmStatusEffect extends MobEffect implements ICharmStatusEffect {
    protected CharmModule module;

    protected CharmStatusEffect(CharmModule module, String name, MobEffectCategory type, int color) {
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
