package svenhjol.charm.module.potion_of_hogsbane;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Items;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.potion.CharmPotion;

public class LongHogsbanePotion extends CharmPotion {
    public LongHogsbanePotion(CharmModule module) {
        super(module, "long_hogsbane", "hogsbane", new StatusEffectInstance(PotionOfHogsbane.HOGSBANE_EFFECT, 9600));
        registerRecipe(PotionOfHogsbane.HOGSPANE_POTION, Items.REDSTONE);
    }
}
