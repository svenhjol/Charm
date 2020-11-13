package svenhjol.charm.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import svenhjol.charm.base.CharmClientModule;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.ClientHandler;
import svenhjol.charm.base.helper.ScreenHelper;
import svenhjol.charm.event.GuiSetupCallback;
import svenhjol.charm.event.RenderGuiCallback;

import java.util.List;
import java.util.function.Consumer;

public class InventoryButtonClient extends CharmClientModule {
    public TexturedButtonWidget recipeButton;
    public PortableCraftingClient portableCraftingClient;
    public PortableEnderChestClient portableEnderChestClient;

    public InventoryButtonClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        GuiSetupCallback.EVENT.register(this::handleGuiSetup);
        RenderGuiCallback.EVENT.register(this::handleRenderGui);

        portableCraftingClient = (PortableCraftingClient)ClientHandler.getModule("charm:portable_crafting");
        portableEnderChestClient = (PortableEnderChestClient)ClientHandler.getModule("charm:portable_ender_chest");
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

        if (portableCraftingClient != null && portableCraftingClient.isButtonVisible()) {
            if (portableEnderChestClient.isButtonVisible()) {
                // recipe, crafting and chest buttons
                if (this.recipeButton != null)
                    this.recipeButton.visible = false;
                portableCraftingClient.craftingButton.setPos(left + 104, y);
                portableEnderChestClient.chestButton.setPos(left + 130, y);

            } else {
                // just the recipe and crafting buttons
                if (this.recipeButton != null)
                    this.recipeButton.visible = true;
                portableCraftingClient.craftingButton.setPos(left + 130, y);

            }
        } else if (portableEnderChestClient != null && portableEnderChestClient.isButtonVisible()) {
            // just the recipe and chest buttons
            if (this.recipeButton != null)
                this.recipeButton.visible = true;
            portableEnderChestClient.chestButton.setPos(left + 130, y);
        }
    }
}
