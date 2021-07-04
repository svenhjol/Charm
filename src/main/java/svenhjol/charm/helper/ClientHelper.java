package svenhjol.charm.helper;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import svenhjol.charm.mixin.accessor.MinecraftAccessor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("unused")
public class ClientHelper {
    public static Options gameOptions;
    public static Font textRenderer;
    public static IconRenderer iconRenderer;
    public static BlockColors blockColors;
    public static Map<ModelLayerLocation, ModelPart> ENTITY_MODEL_LAYERS = new HashMap<>();

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

    public static IconRenderer getIconRenderer() {
        if (iconRenderer == null)
            iconRenderer = new IconRenderer();

        return iconRenderer;
    }

    public static Optional<Options> getGameOptions() {
        if (getClient().isPresent()) {
            if (gameOptions == null)
                gameOptions = Minecraft.getInstance().options;

            return Optional.of(gameOptions);
        }
        return Optional.empty();
    }

    public static class IconRenderer extends GuiComponent {
        public void renderGuiIcon(PoseStack pose, int drawX, int drawY, int offsetX, int offsetY, int sizeX, int sizeY) {
            RenderSystem.setShaderTexture(0, GuiComponent.GUI_ICONS_LOCATION);
            this.blit(pose, drawX, drawY, offsetX, offsetY, sizeX, sizeY);
        }
    }
}
