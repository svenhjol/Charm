package svenhjol.charm.integration.rei;

import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.plugins.REIServerPlugin;
import me.shedaniel.rei.api.common.transfer.info.MenuInfoRegistry;
import me.shedaniel.rei.api.common.transfer.info.simple.RecipeBookGridMenuInfo;
import svenhjol.charm.integration.rei.CharmReiCategories;
import svenhjol.charm.integration.rei.FiringDisplay;
import svenhjol.charm.integration.rei.WoodcuttingDisplay;
import svenhjol.charm.module.kilns.KilnScreenHandler;

public class CharmReiServerPlugin implements REIServerPlugin {
    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        registry.register(svenhjol.charm.integration.rei.CharmReiCategories.WOODCUTTING, WoodcuttingDisplay.serializer());
        registry.register(svenhjol.charm.integration.rei.CharmReiCategories.FIRING, svenhjol.charm.integration.rei.FiringDisplay.serializer(FiringDisplay::new));
    }

    @Override
    public void registerMenuInfo(MenuInfoRegistry registry) {
        registry.register(CharmReiCategories.FIRING, KilnScreenHandler.class, new RecipeBookGridMenuInfo<>());
    }
}
