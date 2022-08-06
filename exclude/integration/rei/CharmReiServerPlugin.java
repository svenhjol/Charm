package svenhjol.charm.integration.rei;

import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.plugins.REIServerPlugin;
import me.shedaniel.rei.api.common.transfer.info.MenuInfoRegistry;
import me.shedaniel.rei.api.common.transfer.info.simple.RecipeBookGridMenuInfo;
import me.shedaniel.rei.api.common.transfer.info.simple.SimpleMenuInfoProvider;
import svenhjol.charm.module.kilns.KilnScreenHandler;

/**
 * @see {https://shedaniel.gitbook.io/roughly-enough-items/api-migration/rei-6-greater-than-rei-7}
 */
public class CharmReiServerPlugin implements REIServerPlugin {
    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        registry.register(CharmReiCategories.WOODCUTTING, WoodcuttingDisplay.serializer());
        registry.register(CharmReiCategories.FIRING, FiringDisplay.serializer(FiringDisplay::new));
    }

    @Override
    public void registerMenuInfo(MenuInfoRegistry registry) {
        registry.register(CharmReiCategories.FIRING, KilnScreenHandler.class, SimpleMenuInfoProvider.of(RecipeBookGridMenuInfo::new));
    }
}
