package svenhjol.charm.module.potion_of_hogsbane;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Items;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.module.potion_of_hogsbane.PotionOfHogsbane;
import svenhjol.charm.potion.CharmPotion;

public class LongHogsbanePotion extends CharmPotion {
    public LongHogsbanePotion(CharmModule module) {
        super(module, "long_hogsbane", "hogsbane", new MobEffectInstance(svenhjol.charm.module.potion_of_hogsbane.PotionOfHogsbane.HOGSBANE_EFFECT, 9600));
        registerRecipe(PotionOfHogsbane.HOGSPANE_POTION, Items.REDSTONE);
    }
}
