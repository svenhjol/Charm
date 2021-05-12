package svenhjol.charm.module;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.ModuleHandler;
import svenhjol.charm.base.iface.Module;

@Module(mod = Charm.MOD_ID, description = "Aerial Affinity is a boots enchantment that increases mining rate when not on the ground.")
public class AerialAffinityEnchantment extends CharmModule {
    public static svenhjol.charm.enchantment.AerialAffinityEnchantment ENCHANTMENT;

    @Override
    public void register() {
        ENCHANTMENT = new svenhjol.charm.enchantment.AerialAffinityEnchantment(this);
    }

    public static boolean digFast(PlayerEntity player) {
        return ModuleHandler.enabled(AerialAffinityEnchantment.class) && EnchantmentHelper.getEquipmentLevel(ENCHANTMENT, player) > 0;
    }
}
