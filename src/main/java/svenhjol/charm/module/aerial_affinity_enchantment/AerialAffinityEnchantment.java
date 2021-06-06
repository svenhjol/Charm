package svenhjol.charm.module.aerial_affinity_enchantment;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.handler.ModuleHandler;
import svenhjol.charm.module.CharmModule;

@Module(mod = Charm.MOD_ID, description = "Aerial Affinity is a boots enchantment that increases mining rate when not on the ground.")
public class AerialAffinityEnchantment extends CharmModule {
    public static AerialAffinityEnch ENCHANTMENT;

    @Override
    public void register() {
        ENCHANTMENT = new AerialAffinityEnch(this);
    }

    public static boolean digFast(Player player) {
        return ModuleHandler.enabled(AerialAffinityEnchantment.class) && EnchantmentHelper.getEnchantmentLevel(ENCHANTMENT, player) > 0;
    }
}
