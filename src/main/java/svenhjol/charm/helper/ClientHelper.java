package svenhjol.charm.helper;

import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.WoodType;
import svenhjol.charm.mixin.accessor.MinecraftAccessor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ClientHelper {
    public static Options gameOptions;
    public static Font textRenderer;
    public static BlockColors blockColors;
    public static Map<ModelLayerLocation, ModelPart> ENTITY_MODEL_LAYERS = new HashMap<>();

    public static <H extends AbstractContainerMenu, S extends Screen & MenuAccess<H>> void registerScreenHandler(MenuType<H> screenHandler, ScreenRegistry.Factory<H, S> screen) {
        ScreenRegistry.register(screenHandler, screen);
    }

    public static void registerSignMaterial(WoodType woodType) {
        Sheets.SIGN_MATERIALS.put(woodType, new Material(Sheets.SIGN_SHEET, new ResourceLocation("entity/signs/" + woodType.name())));
    }

    public static ModelLayerLocation registerEntityModelLayer(ResourceLocation id, ModelPart modelPart) {
        ModelLayerLocation layer = new ModelLayerLocation(id, "main");
        ENTITY_MODEL_LAYERS.put(layer, modelPart);
        return layer;
    }

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
