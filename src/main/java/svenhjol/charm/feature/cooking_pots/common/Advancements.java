package svenhjol.charm.feature.cooking_pots.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.cooking_pots.CookingPots;
import svenhjol.charm.foundation.feature.AdvancementHolder;

public final class Advancements extends AdvancementHolder<CookingPots> {
    public Advancements(CookingPots feature) {
        super(feature);
    }

    public void preparedCookingPot(Player player) {
        trigger("prepared_cooking_pot", player);
    }

    public void tookFoodFromCookingPot(Player player) {
        trigger("took_food_from_cooking_pot", player);
    }
}
