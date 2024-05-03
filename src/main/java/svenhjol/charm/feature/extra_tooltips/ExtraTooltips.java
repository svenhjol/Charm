package svenhjol.charm.feature.extra_tooltips;

import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Registration;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.client.ClientFeature;

import java.util.Optional;

public class ExtraTooltips extends ClientFeature {

    @Configurable(name = "Maps", description = "If true, a map will show its content when hovering over the item.")
    public static boolean showMaps = true;

    @Configurable(
        name = "Shulker boxes",
        description = "If true, the contents of a shulker box will be shown when hovering over the item.")
    public static boolean showShulkerBoxes = true;

    @Override
    public boolean canBeDisabled() {
        return true;
    }

    @Override
    public String description() {
        return "Adds hover tooltips for some items that have content.";
    }

    @Override
    public Optional<Registration<? extends Feature>> registration() {
        return Optional.of(new ClientRegistration(this));
    }
}
