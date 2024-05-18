package svenhjol.charm.feature.compass_shows_position.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.Items;
import svenhjol.charm.feature.compass_shows_position.CompassShowsPosition;
import svenhjol.charm.foundation.feature.FeatureHolder;

public final class Handlers extends FeatureHolder<CompassShowsPosition> {
    public Handlers(CompassShowsPosition feature) {
        super(feature);
    }

    @SuppressWarnings("ConstantValue")
    public void hudRender(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        var minecraft = Minecraft.getInstance();
        if (minecraft == null || minecraft.player == null) return;
        var player = minecraft.player;

        if (!CompassShowsPosition.alwaysShow) {
            // Only render hud if player is holding a compass.
            if (player.getOffhandItem().getItem() != Items.COMPASS && player.getMainHandItem().getItem() != Items.COMPASS) {
                return;
            }
        }

        if (CompassShowsPosition.onlyShowWhenSneaking && !player.isCrouching()) {
            return;
        }

        float y;
        float lineHeight;
        var gui = minecraft.gui;
        var window = minecraft.getWindow();
        var font = gui.getFont();
        var direction = player.getDirection();
        var pos = player.blockPosition();
        var alpha = 220 << 24 & 0xFF000000;

        String coords;

        if (CompassShowsPosition.onlyXZ) {
            coords = I18n.get("gui.charm.coords_only_xz", pos.getX(), pos.getZ());
        } else {
            coords = I18n.get("gui.charm.coords", pos.getX(), pos.getY(), pos.getZ());
        }

        var coordsLength = font.width(coords);
        var coordsColor = 0xAA9988;
        var facing = I18n.get("gui.charm.facing", direction.getName());
        var facingLength = font.width(facing);
        var facingColor = 0xFFEEDD;
        float midX;

        if (CompassShowsPosition.compactView) {
            midX = window.getGuiScaledWidth() / 9.0F;
            y = 10;
            lineHeight = 9;
        } else {
            midX = window.getGuiScaledWidth() / 2.0F;
            y = 40;
            lineHeight = 12;
        }

        if (CompassShowsPosition.showFacing) {
            renderText(guiGraphics, font, facing, midX, y, -facingLength / 2, 0, facingColor | alpha);
            y += lineHeight;
        }

        if (CompassShowsPosition.showCoords) {
            renderText(guiGraphics, font, coords, midX, y, -coordsLength / 2, 0, coordsColor | alpha);
            y += lineHeight;
        }

        if (CompassShowsPosition.showBiome) {
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
    public void renderText(GuiGraphics guiGraphics, Font font, String text, float translateX, float translateY, int offsetX, int offsetY, int color) {
        var pose = guiGraphics.pose();
        var useX = offsetX;

        pose.pushPose();
        pose.translate(translateX, translateY, 0.0);

        if (CompassShowsPosition.compactView) {
            var scaleFactor = 0.75f;
            useX = -32; // Align all text to the left when in compact mode
            pose.scale(scaleFactor, scaleFactor, scaleFactor);
        }

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        guiGraphics.drawString(font, text, useX, offsetY, color);
        RenderSystem.disableBlend();

        pose.popPose();
    }
}
