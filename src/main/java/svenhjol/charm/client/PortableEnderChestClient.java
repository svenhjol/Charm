package svenhjol.charm.client;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.ActionResult;
import svenhjol.charm.base.CharmResources;
import svenhjol.charm.event.RenderGuiCallback;
import svenhjol.charm.event.SetupGuiCallback;
import svenhjol.charm.module.PortableEnderChest;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.ItemHelper;
import svenhjol.meson.helper.ScreenHelper;

public class PortableEnderChestClient {
    private final MesonModule module;
    public TexturedButtonWidget chestButton;

    public PortableEnderChestClient(MesonModule module) {
        this.module = module;

        // set up client listeners
        SetupGuiCallback.EVENT.register((client, width, height, addButton) -> {
            if (client.player == null)
                return ActionResult.PASS;

            if (!(client.currentScreen instanceof InventoryScreen))
                return ActionResult.PASS;

            InventoryScreen screen = (InventoryScreen)client.currentScreen;
            int guiLeft = ScreenHelper.getX(screen);

            this.chestButton = new TexturedButtonWidget(guiLeft + 130, height / 2 - 22, 20, 18, 20, 0, 19, CharmResources.INVENTORY_BUTTONS, click -> {
                ClientSidePacketRegistry.INSTANCE.sendToServer(PortableEnderChest.MSG_SERVER_OPEN_ENDER_CHEST, new PacketByteBuf(Unpooled.buffer()));
            });

            this.chestButton.visible = hasChest(client.player);
            addButton.accept(this.chestButton);

            return ActionResult.PASS;
        });

        RenderGuiCallback.EVENT.register((client, matrices, mouseX, mouseY, delta) -> {
            if (!(client.currentScreen instanceof InventoryScreen)
                || this.chestButton == null
                || client.player == null
            ) {
                return ActionResult.PASS;
            }

            if (client.player.world.getTime() % 5 == 0)
                this.chestButton.visible = hasChest(client.player);

            return ActionResult.PASS;
        });
    }

    private boolean hasChest(PlayerEntity player) {
        return PortableEnderChest.offhandOnly && ItemHelper.getBlockClass(player.getOffHandStack()) == EnderChestBlock.class
            || !PortableEnderChest.offhandOnly && player.inventory.contains(new ItemStack(Blocks.ENDER_CHEST));
    }
}
