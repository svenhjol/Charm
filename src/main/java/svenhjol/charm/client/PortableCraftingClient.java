package svenhjol.charm.client;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import svenhjol.charm.base.CharmResources;
import svenhjol.charm.event.RenderGuiCallback;
import svenhjol.charm.event.SetupGuiCallback;
import svenhjol.charm.module.PortableCrafting;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.ScreenHelper;

public class PortableCraftingClient {
    public TexturedButtonWidget craftingButton;

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
                ClientSidePacketRegistry.INSTANCE.sendToServer(PortableCrafting.MSG_SERVER_OPEN_CRAFTING, new PacketByteBuf(Unpooled.buffer()));
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
    }

    private boolean hasCrafting(PlayerEntity player) {
        return player.inventory.contains(new ItemStack(Blocks.CRAFTING_TABLE));
    }

    public boolean isButtonVisible() {
        return this.craftingButton != null && this.craftingButton.visible;
    }
}
