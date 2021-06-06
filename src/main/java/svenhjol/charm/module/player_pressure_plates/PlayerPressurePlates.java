package svenhjol.charm.module.player_pressure_plates;

import svenhjol.charm.Charm;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.handler.ModuleHandler;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.module.player_pressure_plates.PlayerPressurePlateBlock;

@Module(mod = Charm.MOD_ID, description = "Player-only pressure plates.")
public class PlayerPressurePlates extends CharmModule {
    @Config(name = "Override", description = "This module is automatically disabled if Quark is present. Set true to force enable.")
    public static boolean override = false;

    public static svenhjol.charm.module.player_pressure_plates.PlayerPressurePlateBlock PLAYER_PRESSURE_PLATE_BLOCK;

    @Override
    public boolean depends() {
        return !ModuleHandler.enabled("quark:automation.module.obsidian_plate_module") || override;
    }

    @Override
    public void register() {
        PLAYER_PRESSURE_PLATE_BLOCK = new PlayerPressurePlateBlock(this);
    }
}
