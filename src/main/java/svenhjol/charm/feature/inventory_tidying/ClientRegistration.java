package svenhjol.charm.feature.inventory_tidying;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import svenhjol.charm.Charm;
import svenhjol.charm.api.event.ScreenRenderEvent;
import svenhjol.charm.api.event.ScreenSetupEvent;
import svenhjol.charm.api.iface.IContainerOffsetTweakProvider;
import svenhjol.charm.api.iface.IInventoryTidyingBlacklistProvider;
import svenhjol.charm.api.iface.IInventoryTidyingWhitelistProvider;
import svenhjol.charm.foundation.Registration;
import svenhjol.charm.foundation.helper.ApiHelper;
import svenhjol.charm.foundation.helper.ScreenHelper;

import java.util.ArrayList;
import java.util.List;

import static svenhjol.charm.feature.inventory_tidying.CommonNetworking.C2STidyInventory.*;

public class ClientRegistration extends Registration<InventoryTidyingClient> {
    static final int LEFT = 159;
    static final int TOP = 12;
    static final List<ImageButton> SORTING_BUTTONS = new ArrayList<>();
    static final WidgetSprites TIDY_BUTTON = new WidgetSprites(
        Charm.id("widget/inventory_tidying/tidy_button"),
        Charm.id("widget/inventory_tidying/tidy_button_highlighted")
    );

    public ClientRegistration(InventoryTidyingClient feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        ApiHelper.consume(IContainerOffsetTweakProvider.class,
            provider -> provider.getContainerOffsetTweaks().forEach(
                tweak -> InventoryTidyingClient.CONTAINER_OFFSETS.put(tweak.getScreen(), tweak.getOffset())));

        ApiHelper.consume(IInventoryTidyingWhitelistProvider.class,
            provider -> InventoryTidyingClient.WHITELISTED_SCREENS.addAll(provider.getWhitelistedInventoryTidyingScreens()));

        ApiHelper.consume(IInventoryTidyingBlacklistProvider.class,
            provider -> InventoryTidyingClient.BLACKLISTED_SCREENS.addAll(provider.getBlacklistedInventoryTidyingScreens()));
    }

    @Override
    public void onEnabled() {
        ScreenSetupEvent.INSTANCE.handle(this::handleScreenSetup);
        ScreenRenderEvent.INSTANCE.handle(this::handleScreenRender);
    }

    private void handleScreenSetup(Screen screen) {
        var client = Minecraft.getInstance();

        if (client.player == null) return;
        if (!(screen instanceof AbstractContainerScreen<?> containerScreen)) return;
        if (InventoryTidyingClient.BLACKLISTED_SCREENS.contains(screen.getClass())) return;

        SORTING_BUTTONS.clear();

        var clazz = containerScreen.getClass();
        var menu = containerScreen.getMenu();
        var x = containerScreen.leftPos + LEFT;
        var y = containerScreen.topPos - TOP;

        if (InventoryTidyingClient.CONTAINER_OFFSETS.containsKey(clazz)) {
            var pair = InventoryTidyingClient.CONTAINER_OFFSETS.get(clazz);
            x += pair.getFirst();
            y += pair.getSecond();
        }

        var slots = menu.slots;
        for (var slot : slots) {
            if (InventoryTidyingClient.WHITELISTED_SCREENS.contains(containerScreen.getClass()) && slot.index == 0) {
                addSortingButton(screen, x, y + slot.y,
                    click -> send(TidyType.CONTAINER));
            }

            if (slot.container == client.player.getInventory()) {
                addSortingButton(screen, x, y + slot.y,
                    click -> send(TidyType.PLAYER));
                break;
            }
        }

        SORTING_BUTTONS.forEach(b -> ScreenHelper.addRenderableWidget(containerScreen, b));
    }

    private void handleScreenRender(AbstractContainerScreen<?> screen, GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (InventoryTidyingClient.BLACKLISTED_SCREENS.contains(screen.getClass())) return;

        // Re-render when recipe is opened/closed.
        var x = screen.leftPos;
        SORTING_BUTTONS.forEach(button -> button.setPosition(x + LEFT, button.getY()));
    }

    private void addSortingButton(Screen screen, int x, int y, Button.OnPress callback) {
        SORTING_BUTTONS.add(new ImageButton(x, y, 10, 10, TIDY_BUTTON, callback));
    }
}
