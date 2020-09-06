package svenhjol.charm.client;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import svenhjol.meson.event.RenderGuiCallback;
import svenhjol.meson.event.SetupGuiCallback;
import svenhjol.charm.module.PortableCrafting;
import svenhjol.charm.module.PortableEnderChest;
import svenhjol.meson.helper.ScreenHelper;

public class InventoryButtonClient {
    public TexturedButtonWidget recipeButton;

    public InventoryButtonClient() {
        SetupGuiCallback.EVENT.register(((client, width, height, buttons, addButton) -> {
            Screen currentScreen = client.currentScreen;

            if (!(currentScreen instanceof InventoryScreen))
                return;

            if (!buttons.isEmpty() && buttons.get(0) instanceof TexturedButtonWidget)
                this.recipeButton = (TexturedButtonWidget)buttons.get(0);

            redrawButtons((InventoryScreen)currentScreen);
        }));

        RenderGuiCallback.EVENT.register(((client, matrices, mouseX, mouseY, delta) -> {
            Screen currentScreen = client.currentScreen;
            if (!(currentScreen instanceof InventoryScreen))
                return;

            redrawButtons((InventoryScreen)currentScreen);
        }));
    }

    private void redrawButtons(InventoryScreen screen) {
        int y = screen.height / 2 - 22;
        int left = ScreenHelper.getX(screen);

        if (PortableCrafting.client.isButtonVisible()) {
            if (PortableEnderChest.client.isButtonVisible()) {
                // recipe, crafting and chest buttons
                if (this.recipeButton != null)
                    this.recipeButton.visible = false;
                PortableCrafting.client.craftingButton.setPos(left + 104, y);
                PortableEnderChest.client.chestButton.setPos(left + 130, y);

            } else {
                // just the recipe and crafting buttons
                if (this.recipeButton != null)
                    this.recipeButton.visible = true;
                PortableCrafting.client.craftingButton.setPos(left + 130, y);

            }
        } else if (PortableEnderChest.client.isButtonVisible()) {
            // just the recipe and chest buttons
            if (this.recipeButton != null)
                this.recipeButton.visible = true;
            PortableEnderChest.client.chestButton.setPos(left + 130, y);
        }
    }
}
