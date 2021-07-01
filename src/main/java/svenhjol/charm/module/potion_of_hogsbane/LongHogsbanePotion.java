package svenhjol.charm.module.potion_of_hogsbane;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Items;
import svenhjol.charm.loader.CharmCommonModule;
import svenhjol.charm.potion.CharmPotion;

public class LongHogsbanePotion extends CharmPotion {
    public LongHogsbanePotion(CharmCommonModule module) {
        super(module, "long_hogsbane", "hogsbane", new MobEffectInstance(PotionOfHogsbane.HOGSBANE_EFFECT, 9600));
        registerRecipe(PotionOfHogsbane.HOGSPANE_POTION, Items.REDSTONE);
    }
}
