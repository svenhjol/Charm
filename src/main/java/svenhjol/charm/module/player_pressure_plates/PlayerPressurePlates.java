package svenhjol.charm.module.player_pressure_plates;

import svenhjol.charm.Charm;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.loader.CommonModule;

@Module(mod = Charm.MOD_ID, description = "Player-only pressure plates.")
public class PlayerPressurePlates extends CommonModule {
    @Config(name = "Override", description = "This module is automatically disabled if Quark is present. Set true to force enable.")
    public static boolean override = false;

    public static PlayerPressurePlateBlock PLAYER_PRESSURE_PLATE_BLOCK;

    @Override
    public boolean depends() {
        // TODO: handle Quark integration if/when Quark makes it to Fabric
        return true;
    }

    @Override
    public void register() {
        PLAYER_PRESSURE_PLATE_BLOCK = new PlayerPressurePlateBlock(this);
    }
}
