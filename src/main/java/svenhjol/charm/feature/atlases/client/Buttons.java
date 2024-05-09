package svenhjol.charm.feature.atlases.client;

import net.minecraft.client.gui.components.WidgetSprites;
import svenhjol.charm.Charm;
import svenhjol.charm.foundation.Resolve;

public class Buttons {
    static final WidgetSprites UP_BUTTON = makeButton("up");
    static final WidgetSprites DOWN_BUTTON = makeButton("down");
    static final WidgetSprites LEFT_BUTTON = makeButton("left");
    static final WidgetSprites RIGHT_BUTTON = makeButton("right");
    static final WidgetSprites BACK_BUTTON = makeButton("back");
    static final WidgetSprites ZOOM_IN_BUTTON = makeButton("zoom_in");
    static final WidgetSprites ZOOM_OUT_BUTTON = makeButton("zoom_out");

    private static WidgetSprites makeButton(String name) {
        var instance = Resolve.client(Charm.ID);

        return new WidgetSprites(
            instance.id("widget/atlases/" + name + "_button"),
            instance.id("widget/atlases/" + name + "_button_disabled"),
            instance.id("widget/atlases/" + name + "_button_highlighted"));
    }
}
