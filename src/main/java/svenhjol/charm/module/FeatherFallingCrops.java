package svenhjol.charm.module;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.EnchantmentsHelper;
import svenhjol.meson.iface.Module;

@Module(description = "A player wearing feather falling enchanted boots will not trample crops.")
public class FeatherFallingCrops extends MesonModule {
    public static boolean landedOnFarmlandBlock(Entity entity) {
        return Meson.enabled("charm:feather_falling_crops")
            && entity instanceof LivingEntity
            && EnchantmentsHelper.hasFeatherFalling((LivingEntity)entity);
    }
}
