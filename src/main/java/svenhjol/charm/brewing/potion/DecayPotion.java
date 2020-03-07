package svenhjol.charm.brewing.potion;

import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potions;
import svenhjol.charm.brewing.module.Decay;
import svenhjol.meson.MesonModule;
import svenhjol.meson.MesonPotion;

public class DecayPotion extends MesonPotion {
    public DecayPotion(MesonModule module) {
        super(module, "decay_potion", new EffectInstance(Decay.effect, Decay.duration * 20));

        if (module.enabled)
            registerRecipe(Potions.AWKWARD, Items.WITHER_ROSE);
    }
}
