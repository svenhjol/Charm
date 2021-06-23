package svenhjol.charm.module.potion_of_piercing_vision;

import svenhjol.charm.Charm;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.potion.CharmPotion;

@Module(mod = Charm.MOD_ID, description = "Drink to give yourself the glowing effect or use as a splash potion to light up other entities.")
public class PotionOfPiercingVision extends CharmModule {
    public static CharmPotion PIERCING_VISION_POTION;
    public static CharmPotion LONG_PIERCING_VISION_POTION;

    @Override
    public void register() {
        PIERCING_VISION_POTION = new PiercingVisionPotion(this);
        LONG_PIERCING_VISION_POTION = new LongPiercingVisionPotion(this);
    }
}
