package svenhjol.charm.module.respawn_anchor_in_the_end;

import net.minecraft.world.level.Level;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.loader.CharmModule;

@CommonModule(mod = Charm.MOD_ID, description = "The repsawn anchor can be used in the End.")
public class RespawnAnchorInTheEnd extends CharmModule {
    public static boolean canSetSpawn(Level level) {
        return Charm.LOADER.isEnabled(RespawnAnchorInTheEnd.class) && level.dimension() == Level.END;
    }
}
