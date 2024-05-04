package svenhjol.charm.feature.auto_restock;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.advancements.Advancements;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Registration;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.Optional;

public class AutoRestock extends CommonFeature {
    @Override
    public String description() {
        return "Refills hotbar from your inventory.";
    }

    @Override
    public Optional<Registration<? extends Feature>> registration() {
        return Optional.of(new CommonRegistration(this));
    }

    public static void triggerRestockedCurrentItem(Player player) {
        Advancements.trigger(Charm.id("restocked_current_item"), player);
    }
}
