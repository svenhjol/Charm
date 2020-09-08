package svenhjol.charm.client;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import org.lwjgl.glfw.GLFW;
import svenhjol.charm.base.CharmResources;
import svenhjol.charm.module.PortableCrafting;
import svenhjol.meson.MesonModule;
import svenhjol.meson.event.RenderGuiCallback;
import svenhjol.meson.event.SetupGuiCallback;
import svenhjol.meson.helper.ScreenHelper;

public class PortableCraftingClient {
    public TexturedButtonWidget craftingButton;
    public static KeyBinding keyBinding;

    public PortableCraftingClient(MesonModule module) {
        // set up client listeners
        SetupGuiCallback.EVENT.register(((client, width, height, buttons, addButton) -> {
            if (client.player == null)
                return;

            if (!(client.currentScreen instanceof InventoryScreen))
                return;

            InventoryScreen screen = (InventoryScreen)client.currentScreen;
            int guiLeft = ScreenHelper.getX(screen);

            this.craftingButton = new TexturedButtonWidget(guiLeft + 130, height / 2 - 22, 20, 18, 0, 0, 19, CharmResources.INVENTORY_BUTTONS, click -> {
                triggerOpenCraftingTable();
            });

            this.craftingButton.visible = hasCrafting(client.player);
            addButton.accept(this.craftingButton);
        }));

        RenderGuiCallback.EVENT.register((client, matrices, mouseX, mouseY, delta) -> {
            if (!(client.currentScreen instanceof InventoryScreen)
                || this.craftingButton == null
                || client.player == null
            ) {
                return;
            }

            if (client.player.world.getTime() % 5 == 0)
                this.craftingButton.visible = hasCrafting(client.player);
        });

        if (PortableCrafting.enableKeybind) {
            keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.charm.openCraftingTable",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                "key.categories.inventory"
            ));

            ClientTickEvents.END_WORLD_TICK.register(client -> {
                while (keyBinding.wasPressed()) {
                    triggerOpenCraftingTable();
                }
            });
        }
    }

    private boolean hasCrafting(PlayerEntity player) {
        return player.inventory.contains(new ItemStack(Blocks.CRAFTING_TABLE));
    }

    private void triggerOpenCraftingTable() {
        ClientSidePacketRegistry.INSTANCE.sendToServer(PortableCrafting.MSG_SERVER_OPEN_CRAFTING, new PacketByteBuf(Unpooled.buffer()));
    }

    public boolean isButtonVisible() {
        return this.craftingButton != null && this.craftingButton.visible;
    }
}
