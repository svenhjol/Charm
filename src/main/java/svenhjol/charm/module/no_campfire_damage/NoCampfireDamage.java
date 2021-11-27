package svenhjol.charm.module.no_campfire_damage;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.loader.CharmModule;

@CommonModule(mod = Charm.MOD_ID, description = "Campfires no longer damage mobs.")
public class NoCampfireDamage extends CharmModule {
    @Config(name = "Soul campfires cause damage", description = "If true, soul campfires will still cause damage to mobs.")
    public static boolean soulCampfiresDamage = true;

    public static boolean bypassDamage(BlockState state) {
        if (!Charm.LOADER.isEnabled(NoCampfireDamage.class)) return false;

        if (state.getBlock() == Blocks.SOUL_CAMPFIRE && soulCampfiresDamage) {
            return false;
        }

        return true;
    }
}
