package svenhjol.charm.feature.compass_overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.Items;
import svenhjol.charmony.annotation.Configurable;
import svenhjol.charmony.client.ClientFeature;
import svenhjol.charmony_api.event.HudRenderEvent;

public class CompassOverlay extends ClientFeature {
    @Configurable(name = "Facing", description = "If true, shows the cardinal direction that the player is facing.")
    public static boolean showFacing = true;

    @Configurable(name = "Co-ordinates", description = "If true, shows the player's XYZ coordinates.")
    public static boolean showCoords = true;

    @Configurable(name = "Biome", description = "If true, shows the player's biome.")
    public static boolean showBiome = true;

    @Configurable(name = "Only show X and Z", description = "If true, only show the player's X and Z coordinates (not their height/depth).")
    public static boolean onlyXZ = false;

    @Configurable(name = "Show when sneaking", description = "If true, only show the compass overlay if the player is sneaking.")
    public static boolean onlyShowWhenSneaking = false;

    @Override
    public String description() {
        return "Shows cardinal points and XYZ coordinates when holding a compass.";
    }

    @Override
    public void runWhenEnabled() {
        HudRenderEvent.INSTANCE.handle(this::handleHudRender);
    }

    @SuppressWarnings("ConstantValue")
    private void handleHudRender(GuiGraphics guiGraphics, float tickDelta) {
        var minecraft = Minecraft.getInstance();
        if (minecraft == null || minecraft.player == null) return;

        // Only render hud if player is holding a compass.
        var player = minecraft.player;
        if (player.getOffhandItem().getItem() != Items.COMPASS && player.getMainHandItem().getItem() != Items.COMPASS) {
            return;
        }

        if (CompassOverlay.onlyShowWhenSneaking && !player.isCrouching()) {
            return;
        }

        var y = 40;
        var gui = minecraft.gui;
        var font = gui.getFont();
        var direction = player.getDirection();
        var pos = player.blockPosition();
        var alpha = 220 << 24 & 0xFF000000;

        String coords;

        if (CompassOverlay.onlyXZ) {
            coords = I18n.get("gui.charm.coords_only_xz", pos.getX(), pos.getZ());
        } else {
            coords = I18n.get("gui.charm.coords", pos.getX(), pos.getY(), pos.getZ());
        }

        var coordsLength = font.width(coords);
        var coordsColor = 0xAA9988;
        var facing = I18n.get("gui.charm.facing", direction.getName());
        var facingLength = font.width(facing);
        var facingColor = 0xFFEEDD;
        var midX = gui.screenWidth / 2.0F;

        if (CompassOverlay.showFacing) {
            renderText(guiGraphics, font, facing, midX, y, -facingLength / 2, 0, facingColor | alpha);
            y += 12;
        }

        if (CompassOverlay.showCoords) {
            renderText(guiGraphics, font, coords, midX, y, -coordsLength / 2, 0, coordsColor | alpha);
            y += 12;
        }

        if (CompassOverlay.showBiome) {
            var biomeRes = player.level().getBiome(pos).unwrap().map(key -> key != null ? key.location() : null, unknown -> null);
            if (biomeRes != null) {
                var biomeName = I18n.get("biome." + biomeRes.getNamespace() + "." + biomeRes.getPath());
                var biomeLength = font.width(biomeName);
                var biomeColor = 0x9ACCAA;
                renderText(guiGraphics, font, biomeName, midX, y, -biomeLength / 2, 0, biomeColor | alpha);
            }
        }
    }

    @SuppressWarnings("SameParameterValue")
    private void renderText(GuiGraphics guiGraphics, Font font, String text, float translateX, float translateY, int offsetX, int offsetY, int color) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(translateX, translateY, 0.0);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        guiGraphics.drawString(font, text, offsetX, offsetY, color);
        RenderSystem.disableBlend();
        guiGraphics.pose().popPose();
    }
}
