package svenhjol.charm.base.helper;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;

public class ClientHelper {
    public static void openPlayerInventory() {
        MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.player == null)
            return;

        mc.openScreen(new InventoryScreen(mc.player));
    }
}
