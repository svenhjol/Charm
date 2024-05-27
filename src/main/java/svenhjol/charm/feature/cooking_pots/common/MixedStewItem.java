package svenhjol.charm.feature.cooking_pots.common;

import net.minecraft.world.item.Item;
import svenhjol.charm.feature.cooking_pots.CookingPots;
import svenhjol.charm.foundation.Resolve;
import svenhjol.charm.foundation.item.CharmItem;

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
