package svenhjol.charm.potion;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.potion.CharmPotion;
import svenhjol.charm.module.PotionOfHogsbane;

public class HogsbanePotion extends CharmPotion {
    public HogsbanePotion(CharmModule module) {
        super(module, "hogsbane", new StatusEffectInstance(PotionOfHogsbane.HOGSBANE_EFFECT, 3600));
        registerRecipe(Potions.AWKWARD, Items.WARPED_FUNGUS);
    }
}
