package svenhjol.charm.potion;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.potion.CharmPotion;
import svenhjol.charm.module.PotionOfSpelunking;

public class SpelunkingPotion extends CharmPotion {
    public SpelunkingPotion(CharmModule module) {
        super(module, "spelunking", new StatusEffectInstance(PotionOfSpelunking.SPELUNKING_EFFECT, PotionOfSpelunking.duration * 20));
        registerRecipe(Potions.AWKWARD, Items.AMETHYST_SHARD);
    }
}
