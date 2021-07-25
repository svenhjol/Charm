package svenhjol.charm.module.portable_crafting;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.glfw.GLFW;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.event.RenderGuiCallback;
import svenhjol.charm.event.SetupGuiCallback;
import svenhjol.charm.helper.ScreenRenderHelper;
import svenhjol.charm.init.CharmResources;
import svenhjol.charm.init.CharmTags;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.mixin.accessor.PlayerAccessor;

import java.util.List;

@ClientModule(module = PortableCrafting.class)
public class PortableCraftingClient extends CharmModule {
    public ImageButton craftingButton;
    public static KeyMapping keyBinding;

    @Override
    public void runWhenEnabled() {
        // set up client listeners
        SetupGuiCallback.EVENT.register(this::handleGuiSetup);
        RenderGuiCallback.EVENT.register(this::handleRenderGui);

        if (PortableCrafting.enableKeybind) {
            keyBinding = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.charm.openCraftingTable",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                "key.categories.inventory"
            ));

            ClientTickEvents.END_WORLD_TICK.register(world -> {
                if (keyBinding == null || world == null)
                    return;

                while (keyBinding.consumeClick()) {
                    triggerOpenCraftingTable();
                }
            });
        }
    }

    private void handleGuiSetup(Minecraft client, int width, int height, List<NarratableEntry> buttons) {
        if (client.player == null)
            return;

        if (!(client.screen instanceof InventoryScreen))
            return;

        InventoryScreen screen = (InventoryScreen)client.screen;
        int guiLeft = ScreenRenderHelper.getX(screen);

        this.craftingButton = new ImageButton(guiLeft + 130, height / 2 - 22, 20, 18, 0, 0, 19, CharmResources.INVENTORY_BUTTONS, click -> {
            triggerOpenCraftingTable();
        });

        this.craftingButton.visible = hasCrafting(client.player);
        screen.addRenderableWidget(this.craftingButton);
    }

    private void handleRenderGui(Minecraft client, PoseStack matrices, int mouseX, int mouseY, float delta) {
        if (!(client.screen instanceof InventoryScreen)
            || this.craftingButton == null
            || client.player == null
        ) {
            return;
        }

        if (client.player.level.getGameTime() % 5 == 0)
            this.craftingButton.visible = hasCrafting(client.player);
    }

    private boolean hasCrafting(Player player) {
        return ((PlayerAccessor)player).getInventory().contains(CharmTags.CRAFTING_TABLES);
    }

    private void triggerOpenCraftingTable() {
        ClientPlayNetworking.send(PortableCrafting.MSG_SERVER_OPEN_CRAFTING, new FriendlyByteBuf(Unpooled.buffer()));
    }

    public boolean isButtonVisible() {
        return this.craftingButton != null && this.craftingButton.visible;
    }
}
