package svenhjol.charm.module.potion_of_spelunking;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.potion.CharmPotion;

public class SpelunkingPotion extends CharmPotion {
    public SpelunkingPotion(CharmModule module) {
        super(module, "spelunking", new StatusEffectInstance(PotionOfSpelunking.SPELUNKING_EFFECT, PotionOfSpelunking.duration * 20));
        registerRecipe(Potions.AWKWARD, Items.AMETHYST_SHARD);
    }
}
