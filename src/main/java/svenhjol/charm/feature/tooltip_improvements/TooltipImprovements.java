package svenhjol.charm.feature.tooltip_improvements;

import svenhjol.charm.feature.tooltip_improvements.client.Handlers;
import svenhjol.charm.feature.tooltip_improvements.client.Registers;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;

@Feature(description = "Adds hover tooltips for some items that have content.")
public final class TooltipImprovements extends ClientFeature {
    public final Handlers handlers;
    public final Registers registers;

    @Configurable(name = "Maps", description = "If true, a map will show its content when hovering over the item.")
    public static boolean showMaps = true;

    @Configurable(
        name = "Shulker boxes",
        description = "If true, the contents of a shulker box will be shown when hovering over the item.")
    public static boolean showShulkerBoxes = true;

    public TooltipImprovements(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
    }

    @Override
    public boolean canBeDisabled() {
        return true;
    }
}
