package svenhjol.charm.feature.cooking_pots.common;

import net.minecraft.world.item.Item;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.charmony.common.item.CharmItem;
import svenhjol.charm.feature.cooking_pots.CookingPots;

public class MixedStewItem extends CharmItem<CookingPots> {
    private static final CookingPots COOKING_POTS = Resolve.feature(CookingPots.class);

    public MixedStewItem() {
        super(new Item.Properties()
            .stacksTo(COOKING_POTS.stewStackSize())
            .food(COOKING_POTS.registers.mixedStewFoodProperties));
    }

    @Override
    public Class<CookingPots> typeForFeature() {
        return CookingPots.class;
    }
}
