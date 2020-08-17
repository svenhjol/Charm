package svenhjol.charm.module;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

@Module(description = "Campfires no longer damage mobs.")
public class CampfiresNoDamage extends MesonModule {
    @Config(name = "Soul campfires cause damage", description = "If true, soul campfires will still cause damage to mobs.")
    public static boolean soulCampfiresDamage = true;

    public static boolean bypassDamage(BlockState state) {
        if (!Meson.enabled("charm:campfires_no_damage"))
            return false;

        if (state.getBlock() == Blocks.SOUL_CAMPFIRE && soulCampfiresDamage)
            return false;

        return true;
    }
}
