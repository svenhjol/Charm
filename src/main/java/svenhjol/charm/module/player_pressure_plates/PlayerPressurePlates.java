package svenhjol.charm.module.player_pressure_plates;

import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.module.extra_wandering_trades.ExtraWanderingTrades;

@CommonModule(mod = Charm.MOD_ID, description = "Player-only pressure plates crafted using gilded blackstone.")
public class PlayerPressurePlates extends CharmModule {
    public static PlayerPressurePlateBlock PLAYER_PRESSURE_PLATE_BLOCK;

    @Override
    public void register() {
        PLAYER_PRESSURE_PLATE_BLOCK = new PlayerPressurePlateBlock(this);
        ExtraWanderingTrades.registerItem(PLAYER_PRESSURE_PLATE_BLOCK, 2, 8);
    }
}
