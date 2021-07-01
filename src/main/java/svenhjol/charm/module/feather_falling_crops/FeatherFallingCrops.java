package svenhjol.charm.module.feather_falling_crops;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import svenhjol.charm.Charm;
import svenhjol.charm.loader.CommonModule;
import svenhjol.charm.helper.EnchantmentsHelper;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.annotation.Module;

@Module(mod = Charm.MOD_ID, description = "A player wearing feather falling enchanted boots will not trample crops.")
public class FeatherFallingCrops extends CommonModule {

    @Config(name = "Requires feather falling", description = "Set to false to prevent trampling even when the player does not wear feather falling boots.")
    public static boolean requiresFeatherFalling = true;

    @Config(name = "Villagers never trample crops", description = "If true, villagers will never trample crops.")
    public static boolean villagersNeverTrampleCrops = true;

    public static boolean landedOnFarmlandBlock(Entity entity) {
        if (Charm.LOADER.isEnabled("charm:feather_falling_crops") && entity instanceof LivingEntity) {
            if (entity instanceof Player && (!requiresFeatherFalling || EnchantmentsHelper.hasFeatherFalling((LivingEntity) entity)))
                return true;

            if (entity instanceof Villager && villagersNeverTrampleCrops)
                return true;
        }
        return false;
    }
}
