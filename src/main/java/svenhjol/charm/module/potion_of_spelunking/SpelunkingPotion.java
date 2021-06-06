package svenhjol.charm.module.potion_of_spelunking;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.potion.CharmPotion;

public class SpelunkingPotion extends CharmPotion {
    public SpelunkingPotion(CharmModule module) {
        super(module, "spelunking", new MobEffectInstance(PotionOfSpelunking.SPELUNKING_EFFECT, PotionOfSpelunking.duration * 20));
        registerRecipe(Potions.AWKWARD, Items.AMETHYST_SHARD);
    }
}
