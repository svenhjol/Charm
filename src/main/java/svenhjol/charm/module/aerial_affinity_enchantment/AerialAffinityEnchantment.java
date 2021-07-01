package svenhjol.charm.module.aerial_affinity_enchantment;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;

@CommonModule(mod = Charm.MOD_ID, description = "Aerial Affinity is a boots enchantment that increases mining rate when not on the ground.")
public class AerialAffinityEnchantment extends svenhjol.charm.loader.CommonModule {
    public static AerialAffinityEnch ENCHANTMENT;

    @Override
    public void register() {
        ENCHANTMENT = new AerialAffinityEnch(this);
    }

    public static boolean digFast(Player player) {
        return Charm.LOADER.isEnabled(AerialAffinityEnchantment.class) && EnchantmentHelper.getEnchantmentLevel(ENCHANTMENT, player) > 0;
    }
}
