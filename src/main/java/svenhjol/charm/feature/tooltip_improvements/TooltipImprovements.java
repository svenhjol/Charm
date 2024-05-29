package svenhjol.charm.feature.tooltip_improvements;

import svenhjol.charm.feature.tooltip_improvements.client.Handlers;
import svenhjol.charm.feature.tooltip_improvements.client.Registers;
import svenhjol.charm.charmony.annotation.Configurable;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;

@Feature(description = "Adds hover tooltips for some items that have content.")
public final class TooltipImprovements extends ClientFeature {
    public final Handlers handlers;
    public final Registers registers;

    @Configurable(name = "Maps", description = "If true, a map will show its content when hovering over the item.")
    private static boolean showMaps = true;

    @Configurable(
        name = "Shulker boxes",
        description = "If true, the contents of a shulker box will be shown when hovering over the item.")
    private static boolean showShulkerBoxes = true;

    public TooltipImprovements(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
    }

    public boolean showMaps() {
        return showMaps;
    }

    public boolean showShulkerBoxes() {
        return showShulkerBoxes;
    }


    @Override
    public boolean canBeDisabled() {
        return true;
    }
}
