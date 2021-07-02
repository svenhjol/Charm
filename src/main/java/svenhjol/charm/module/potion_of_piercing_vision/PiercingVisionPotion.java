package svenhjol.charm.module.potion_of_piercing_vision;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.potion.CharmPotion;

public class PiercingVisionPotion extends CharmPotion {
    public PiercingVisionPotion(CharmModule module) {
        super(module, "piercing_vision", new MobEffectInstance(MobEffects.GLOWING, 3600));
        registerRecipe(Potions.AWKWARD, Items.GLOW_INK_SAC);
    }
}
