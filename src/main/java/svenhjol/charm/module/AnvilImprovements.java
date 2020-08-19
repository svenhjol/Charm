package svenhjol.charm.module;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.Property;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

import java.util.Random;

@Module(description = "Removes minimum and maximum XP costs on the anvil. Anvils are also less likely to break.")
public class AnvilImprovements extends MesonModule {
    @Config(name = "Remove Too Expensive", description = "If true, removes the maximum cost of 40 XP when working items on the anvil.")
    public static boolean removeTooExpensive = true;

    @Config(name = "Stronger anvils", description = "If true, anvils are 50% less likely to take damage when used.")
    public static boolean strongerAnvils = true;

    public static boolean allowTooExpensive() {
        return Meson.enabled("charm:anvil_improvements") && AnvilImprovements.removeTooExpensive;
    }

    public static boolean allowTakeWithoutXp(PlayerEntity player, Property levelCost) {
        return Meson.enabled("charm:anvil_improvements")
            && (player.abilities.creativeMode || ((player.experienceLevel >= levelCost.get()) && levelCost.get() > -1));
    }

    public static boolean tryDamageAnvil() {
        return Meson.enabled("charm:anvil_improvements")
            && AnvilImprovements.strongerAnvils
            && new Random().nextFloat() < 0.5F;
    }
}
