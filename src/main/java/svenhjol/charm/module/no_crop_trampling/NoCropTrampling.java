package svenhjol.charm.module.no_crop_trampling;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.helper.EnchantmentsHelper;
import svenhjol.charm.loader.CharmModule;

@CommonModule(mod = Charm.MOD_ID)
public class NoCropTrampling extends CharmModule {

    @Config(name = "Require feather falling", description = "If true, a player or mob will trample crops unless wearing boots with feather falling enchantment.")
    public static boolean requireFeatherFalling = false;

    public static boolean shouldNotTrample(Entity entity) {
        if (!Charm.LOADER.isEnabled(NoCropTrampling.class) || !(entity instanceof LivingEntity livingEntity)) {
            return false;
        }

        if (requireFeatherFalling && !EnchantmentsHelper.hasFeatherFalling(livingEntity)) {
            return false;
        }

        return true;
    }
}
