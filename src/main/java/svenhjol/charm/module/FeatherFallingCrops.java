package svenhjol.charm.module;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import svenhjol.charm.Charm;
import svenhjol.charm.base.handler.ModuleHandler;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.helper.EnchantmentsHelper;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;

@Module(mod = Charm.MOD_ID, description = "A player wearing feather falling enchanted boots will not trample crops.")
public class FeatherFallingCrops extends CharmModule {

    @Config(name = "Requires feather falling", description = "Turn this off to prevent trampling even when the player does not wear feather falling boots.")
    public static boolean requiresFeatherFalling = true;

    public static boolean landedOnFarmlandBlock(Entity entity) {
        return ModuleHandler.enabled("charm:feather_falling_crops")
            && entity instanceof LivingEntity
            && (!requiresFeatherFalling || EnchantmentsHelper.hasFeatherFalling((LivingEntity)entity));
    }
}
