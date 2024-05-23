package svenhjol.charm.feature.item_tidying.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.item_tidying.ItemTidyingClient;
import svenhjol.charm.feature.item_tidying.common.TidyType;
import svenhjol.charm.foundation.feature.FeatureHolder;

import java.util.ArrayList;
import java.util.List;

import static svenhjol.charm.feature.item_tidying.common.Networking.C2STidyInventory.send;

public final class Handlers extends FeatureHolder<ItemTidyingClient> {
    private static final int LEFT = 159;
    private static final int TOP = 12;

    private final List<ImageButton> sortingButtons = new ArrayList<>();
    private final WidgetSprites tidyButton = new WidgetSprites(
        Charm.id("widget/item_tidying/tidy_button"),
        Charm.id("widget/item_tidying/tidy_button_highlighted")
    );

    public Handlers(ItemTidyingClient feature) {
        super(feature);
    }

    public void screenSetup(Screen screen) {
        var client = Minecraft.getInstance();

        if (client.player == null) return;
        if (!(screen instanceof AbstractContainerScreen<?> containerScreen)) return;
        if (feature().providers.blacklistedScreens.contains(screen.getClass())) return;

        sortingButtons.clear();

        var clazz = containerScreen.getClass();
        var menu = containerScreen.getMenu();
        var x = containerScreen.leftPos + LEFT;
        var y = containerScreen.topPos - TOP;

        if (feature().providers.containerOffsets.containsKey(clazz)) {
            var pair = feature().providers.containerOffsets.get(clazz);
            x += pair.getFirst();
            y += pair.getSecond();
        }

        var slots = menu.slots;
        for (var slot : slots) {
            if (feature().providers.whitelistedScreens.contains(containerScreen.getClass()) && slot.index == 0) {
                addSortingButton(screen, x, y + slot.y,
                    click -> send(TidyType.CONTAINER));
            }

            if (slot.container == client.player.getInventory()) {
                addSortingButton(screen, x, y + slot.y,
                    click -> send(TidyType.PLAYER));
                break;
            }
        }

        sortingButtons.forEach(containerScreen::addRenderableWidget);
    }

    public void screenRender(AbstractContainerScreen<?> screen, GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (feature().providers.blacklistedScreens.contains(screen.getClass())) return;

        // Re-render when recipe is opened/closed.
        var x = screen.leftPos;
        sortingButtons.forEach(button -> button.setPosition(x + LEFT, button.getY()));
    }

    public void addSortingButton(Screen screen, int x, int y, Button.OnPress callback) {
        sortingButtons.add(new ImageButton(x, y, 10, 10, tidyButton, callback));
    }
}
