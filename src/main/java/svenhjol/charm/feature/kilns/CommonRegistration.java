package svenhjol.charm.feature.kilns;

import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import svenhjol.charm.foundation.feature.Register;

import java.util.List;

public final class CommonRegistration extends Register<Kilns> {
    static final String BLOCK_ID = "kiln";

    public CommonRegistration(Kilns feature) {
        super(feature);

        // Early registration of enums.
        feature.registry().recipeBookTypeEnum("kiln");
    }

    @Override
    public void onRegister() {
        var registry = feature.registry();

        Kilns.block = registry.block(BLOCK_ID, KilnBlock::new);
        Kilns.blockItem = registry.item(BLOCK_ID, () -> new KilnBlock.BlockItem(Kilns.block));
        Kilns.blockEntity = registry.blockEntity(BLOCK_ID, () -> KilnBlockEntity::new, List.of(Kilns.block));

        Kilns.recipeBookType = registry.recipeBookType(BLOCK_ID);
        Kilns.menu = registry.menuType(BLOCK_ID, () -> new MenuType<>(KilnMenu::new, FeatureFlags.VANILLA_SET));

        Kilns.bakeSound = registry.soundEvent("kiln_bake");
    }
}
