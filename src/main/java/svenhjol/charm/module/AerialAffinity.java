package svenhjol.charm.module;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.ModuleHandler;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.enchantment.AerialAffinityEnchantment;

@Module(mod = Charm.MOD_ID, description = "Aerial Affinity is a boots enchantment that increases mining rate when not on the ground.")
public class AerialAffinity extends CharmModule {
    public static AerialAffinityEnchantment AERIAL_AFFINITY;

    @Override
    public void register() {
        AERIAL_AFFINITY = new AerialAffinityEnchantment(this);
    }

    public static boolean digFast(PlayerEntity player) {
        return ModuleHandler.enabled(AerialAffinity.class) && EnchantmentHelper.getEquipmentLevel(AERIAL_AFFINITY, player) > 0;
    }
}
