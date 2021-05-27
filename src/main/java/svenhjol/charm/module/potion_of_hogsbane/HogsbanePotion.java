package svenhjol.charm.module.potion_of_hogsbane;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.potion.CharmPotion;

public class HogsbanePotion extends CharmPotion {
    public HogsbanePotion(CharmModule module) {
        super(module, "hogsbane", new StatusEffectInstance(PotionOfHogsbane.HOGSBANE_EFFECT, 3600));
        registerRecipe(Potions.AWKWARD, Items.WARPED_FUNGUS);
    }
}
