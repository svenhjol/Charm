package svenhjol.charm.base.potion;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import svenhjol.charm.base.CharmModule;

public abstract class CharmStatusEffect extends StatusEffect implements ICharmStatusEffect {
    protected CharmModule module;

    protected CharmStatusEffect(CharmModule module, String name, StatusEffectType type, int color) {
        super(type, color);
        this.module = module;
        this.register(module, name);
    }

    protected void addStatusEffect(StatusEffect effect) {
        effect.getAttributeModifiers().forEach((attribute, modifier) -> {
            this.addAttributeModifier(attribute, modifier.getId().toString(), modifier.getValue(), modifier.getOperation());
        });
    }
}
