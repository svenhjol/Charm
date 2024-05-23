package svenhjol.charm.feature.crafting_from_inventory.client;

import net.minecraft.client.gui.components.WidgetSprites;
import svenhjol.charm.Charm;

@SuppressWarnings("SameParameterValue")
public class Buttons {
    static final WidgetSprites CRAFTING_BUTTON = makeButton("crafting");

    private static WidgetSprites makeButton(String name) {
        return new WidgetSprites(
            Charm.id("widget/crafting_from_inventory/" + name + "_button"),
            Charm.id("widget/crafting_from_inventory/" + name + "_button_highlighted"));
    }
}
