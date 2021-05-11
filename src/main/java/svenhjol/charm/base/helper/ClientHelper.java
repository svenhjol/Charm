package svenhjol.charm.base.helper;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import svenhjol.charm.mixin.accessor.MinecraftClientAccessor;

import java.util.Optional;

public class ClientHelper {
    public static GameOptions gameOptions;
    public static TextRenderer textRenderer;
    public static BlockColors blockColors;

    public static void openPlayerInventory() {
        MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.player == null)
            return;

        mc.openScreen(new InventoryScreen(mc.player));
    }

    public static <T extends BlockEntity> double getBlockEntityDistance(PlayerEntity player, T entity, Camera camera) {
        int x = entity.getPos().getX();
        int y = entity.getPos().getY();
        int z = entity.getPos().getZ();
        return camera.getPos().squaredDistanceTo(x, y, z);
    }

    public static Optional<MinecraftClient> getClient() {
        return Optional.ofNullable(MinecraftClient.getInstance());
    }

    public static Optional<World> getWorld() {
        if (!getClient().isPresent())
            return Optional.empty();

        return Optional.ofNullable(getClient().get().world);
    }

    public static Optional<PlayerEntity> getPlayer() {
        if (!getClient().isPresent())
            return Optional.empty();

        return Optional.ofNullable(getClient().get().player);
    }

    public static Optional<BlockColors> getBlockColors() {
        if (getClient().isPresent()) {
            if (blockColors == null)
                blockColors = ((MinecraftClientAccessor) MinecraftClient.getInstance()).getBlockColors();

            return Optional.of(blockColors);
        }
        return Optional.empty();
    }

    public static Optional<TextRenderer> getTextRenderer() {
        if (getClient().isPresent()) {
            if (textRenderer == null)
                textRenderer = MinecraftClient.getInstance().textRenderer;

            return Optional.of(textRenderer);
        }
        return Optional.empty();
    }

    public static Optional<GameOptions> getGameOptions() {
        if (getClient().isPresent()) {
            if (gameOptions == null)
                gameOptions = MinecraftClient.getInstance().options;

            return Optional.of(gameOptions);
        }
        return Optional.empty();
    }
}
