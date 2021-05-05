package svenhjol.charm.potion;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Items;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.potion.CharmPotion;
import svenhjol.charm.module.PotionOfHogsbane;

public class LongHogsbanePotion extends CharmPotion {
    public LongHogsbanePotion(CharmModule module) {
        super(module, "long_hogsbane", "hogsbane", new StatusEffectInstance(PotionOfHogsbane.HOGSBANE_EFFECT, 9600));
        registerRecipe(PotionOfHogsbane.HOGSPANE_POTION, Items.REDSTONE);
    }
}
