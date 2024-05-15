package svenhjol.charm.feature.atlases.client;

import net.minecraft.client.gui.components.WidgetSprites;
import svenhjol.charm.Charm;

public class Buttons {
    static final WidgetSprites UP_BUTTON = makeButton("up");
    static final WidgetSprites DOWN_BUTTON = makeButton("down");
    static final WidgetSprites LEFT_BUTTON = makeButton("left");
    static final WidgetSprites RIGHT_BUTTON = makeButton("right");
    static final WidgetSprites BACK_BUTTON = makeButton("back");
    static final WidgetSprites ZOOM_IN_BUTTON = makeButton("zoom_in");
    static final WidgetSprites ZOOM_OUT_BUTTON = makeButton("zoom_out");

    private static WidgetSprites makeButton(String name) {
        return new WidgetSprites(
            Charm.id("widget/atlases/" + name + "_button"),
            Charm.id("widget/atlases/" + name + "_button_disabled"),
            Charm.id("widget/atlases/" + name + "_button_highlighted"));
    }
}
