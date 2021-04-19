package svenhjol.charm.base.helper;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import java.util.Optional;

public class ClientHelper {
    public static void openPlayerInventory() {
        MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.player == null)
            return;

        mc.openScreen(new InventoryScreen(mc.player));
    }

    public static Optional<MinecraftClient> getClient() {
        final MinecraftClient mc = MinecraftClient.getInstance();
        if (mc == null)
            return Optional.empty();

        return Optional.of(mc);
    }

    public static Optional<World> getWorld() {
        if (getClient().isPresent()) {
            ClientWorld world = getClient().get().world;
            if (world != null)
                return Optional.of(world);
        }

        return Optional.empty();
    }

    public static Optional<PlayerEntity> getPlayer() {
        if (getClient().isPresent()) {
            ClientPlayerEntity player = getClient().get().player;
            if (player != null)
                return Optional.of(player);
        }

        return Optional.empty();
    }
}
