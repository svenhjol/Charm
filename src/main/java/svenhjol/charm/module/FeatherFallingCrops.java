package svenhjol.charm.module;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import svenhjol.charm.Charm;
import svenhjol.charm.base.handler.ModuleHandler;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.helper.EnchantmentsHelper;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;

@Module(mod = Charm.MOD_ID, description = "A player wearing feather falling enchanted boots will not trample crops.")
public class FeatherFallingCrops extends CharmModule {

    @Config(name = "Requires feather falling", description = "Set to false to prevent trampling even when the player does not wear feather falling boots.")
    public static boolean requiresFeatherFalling = true;

    @Config(name = "Villagers never trample crops", description = "If true, villagers will never trample crops.")
    public static boolean villagersNeverTrampleCrops = true;

    public static boolean landedOnFarmlandBlock(Entity entity) {
        if (ModuleHandler.enabled("charm:feather_falling_crops") && entity instanceof LivingEntity) {
            if (entity instanceof PlayerEntity && (!requiresFeatherFalling || EnchantmentsHelper.hasFeatherFalling((LivingEntity) entity)))
                return true;

            if (entity instanceof VillagerEntity && villagersNeverTrampleCrops)
                return true;
        }
        return false;
    }
}
