package svenhjol.charm.module.potion_of_piercing_vision;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Items;
import svenhjol.charm.loader.CharmCommonModule;
import svenhjol.charm.potion.CharmPotion;

public class LongPiercingVisionPotion extends CharmPotion {
    public LongPiercingVisionPotion(CharmCommonModule module) {
        super(module, "long_piercing_vision", "piercing_vision", new MobEffectInstance(MobEffects.GLOWING, 9600));
        registerRecipe(PotionOfPiercingVision.PIERCING_VISION_POTION, Items.REDSTONE);
    }
}
