package svenhjol.charm.helper;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import svenhjol.charm.mixin.accessor.MinecraftAccessor;

import java.util.Optional;

public class ClientHelper {
    public static Options gameOptions;
    public static Font textRenderer;
    public static BlockColors blockColors;

    public static void openPlayerInventory() {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null)
            return;

        mc.setScreen(new InventoryScreen(mc.player));
    }

    public static <T extends BlockEntity> double getBlockEntityDistance(Player player, T entity, Camera camera) {
        int x = entity.getBlockPos().getX();
        int y = entity.getBlockPos().getY();
        int z = entity.getBlockPos().getZ();
        return camera.getPosition().distanceToSqr(x, y, z);
    }

    public static Optional<Minecraft> getClient() {
        return Optional.ofNullable(Minecraft.getInstance());
    }

    public static Optional<Level> getWorld() {
        if (getClient().isEmpty())
            return Optional.empty();

        return Optional.ofNullable(getClient().get().level);
    }

    public static Optional<Player> getPlayer() {
        if (getClient().isEmpty())
            return Optional.empty();

        return Optional.ofNullable(getClient().get().player);
    }

    public static Optional<BlockColors> getBlockColors() {
        if (getClient().isPresent()) {
            if (blockColors == null)
                blockColors = ((MinecraftAccessor) Minecraft.getInstance()).getBlockColors();

            return Optional.of(blockColors);
        }
        return Optional.empty();
    }

    public static Optional<Font> getTextRenderer() {
        if (getClient().isPresent()) {
            if (textRenderer == null)
                textRenderer = Minecraft.getInstance().font;

            return Optional.of(textRenderer);
        }
        return Optional.empty();
    }

    public static Optional<Options> getGameOptions() {
        if (getClient().isPresent()) {
            if (gameOptions == null)
                gameOptions = Minecraft.getInstance().options;

            return Optional.of(gameOptions);
        }
        return Optional.empty();
    }
}
