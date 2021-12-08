package svenhjol.charm.module.respawn_anchor_in_the_end;

import net.minecraft.world.level.Level;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.loader.CharmModule;

@CommonModule(mod = Charm.MOD_ID, enabledByDefault = false, description = "The repsawn anchor can be used in the End.\n" +
    "This is an opinionated feature that changes a core gameplay feature and so is disabled by default.")
public class RespawnAnchorInTheEnd extends CharmModule {
    public static boolean canSetSpawn(Level level) {
        return Charm.LOADER.isEnabled(RespawnAnchorInTheEnd.class) && level.dimension() == Level.END;
    }
}
