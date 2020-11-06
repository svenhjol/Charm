package svenhjol.charm.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import svenhjol.charm.base.helper.ScreenHelper;
import svenhjol.charm.event.GuiSetupCallback;
import svenhjol.charm.event.RenderGuiCallback;
import svenhjol.charm.module.PortableCrafting;
import svenhjol.charm.module.PortableEnderChest;

import java.util.List;
import java.util.function.Consumer;

public class InventoryButtonClient {
    public TexturedButtonWidget recipeButton;

    public InventoryButtonClient() {
        GuiSetupCallback.EVENT.register(this::handleGuiSetup);
        RenderGuiCallback.EVENT.register(this::handleRenderGui);
    }

    private void handleGuiSetup(MinecraftClient client, int width, int height, List<AbstractButtonWidget> buttons, Consumer<AbstractButtonWidget> addButton) {
        Screen currentScreen = client.currentScreen;

        if (!(currentScreen instanceof InventoryScreen))
            return;

        if (!buttons.isEmpty() && buttons.get(0) instanceof TexturedButtonWidget)
            this.recipeButton = (TexturedButtonWidget)buttons.get(0);

        redrawButtons((InventoryScreen)currentScreen);
    }

    private void handleRenderGui(MinecraftClient client, MatrixStack matrices, int mouseX, int mouseY, float delta) {
        Screen currentScreen = client.currentScreen;
        if (!(currentScreen instanceof InventoryScreen))
            return;

        redrawButtons((InventoryScreen)currentScreen);
    }

    private void redrawButtons(InventoryScreen screen) {
        int y = screen.height / 2 - 22;
        int left = ScreenHelper.getX(screen);

        if (PortableCrafting.client != null && PortableCrafting.client.isButtonVisible()) {
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
        } else if (PortableEnderChest.client != null && PortableEnderChest.client.isButtonVisible()) {
            // just the recipe and chest buttons
            if (this.recipeButton != null)
                this.recipeButton.visible = true;
            PortableEnderChest.client.chestButton.setPos(left + 130, y);
        }
    }
}
