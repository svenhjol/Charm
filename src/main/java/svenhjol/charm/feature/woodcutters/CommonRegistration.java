package svenhjol.charm.feature.woodcutters;

import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import svenhjol.charm.foundation.feature.Register;

public final class CommonRegistration extends Register<Woodcutters> {
    public static final String BLOCK_ID = "woodcutter";

    public CommonRegistration(Woodcutters feature) {
        super(feature);

        // Register enum early
        feature.registry().recipeBookTypeEnum("woodcutter");
    }

    @Override
    public void onRegister() {
        var registry = feature.registry();

        Woodcutters.block = registry.block(BLOCK_ID,
            () -> new WoodcutterBlock(feature));

        Woodcutters.blockItem = registry.item(BLOCK_ID,
            () -> new WoodcutterBlock.BlockItem(Woodcutters.block));

        Woodcutters.poiType = registry.pointOfInterestType(BLOCK_ID,
            () -> new PoiType(ImmutableSet.copyOf(Woodcutters.block.get().getStateDefinition().getPossibleStates()),
                1, 1));

        Woodcutters.recipeBookType = registry.recipeBookType(BLOCK_ID);

        Woodcutters.menu = registry.menuType(BLOCK_ID,
            () -> new MenuType<>(WoodcutterMenu::new, FeatureFlags.VANILLA_SET));

        Woodcutters.useSound = registry.soundEvent("woodcutter_use");
    }
}
