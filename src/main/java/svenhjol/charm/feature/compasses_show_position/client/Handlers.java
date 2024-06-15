package svenhjol.charm.feature.compasses_show_position.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import svenhjol.charm.charmony.feature.FeatureHolder;
import svenhjol.charm.feature.compasses_show_position.CompassesShowPosition;

public final class Handlers extends FeatureHolder<CompassesShowPosition> {
    public Handlers(CompassesShowPosition feature) {
        super(feature);
    }

    @SuppressWarnings("ConstantValue")
    public void hudRender(GuiGraphics guiGraphics, float tickDelta) {
        var minecraft = Minecraft.getInstance();
        if (minecraft == null || minecraft.player == null) return;
        var player = minecraft.player;
        
        // Don't show if Hide GUI is enabled.
        if (minecraft.options.hideGui) {
            return;
        }

        if (!feature().alwaysShow()) {
            // Only render hud if player is holding a compass.
            if (player.getOffhandItem().getItem() != Items.COMPASS && player.getMainHandItem().getItem() != Items.COMPASS) {
                return;
            }
        }

        if (feature().onlyShowWhenSneaking() && !player.isCrouching()) {
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

        if (feature().onlyShowXZ()) {
            coords = I18n.get("gui.charm.compass.coords_only_xz", pos.getX(), pos.getZ());
        } else {
            coords = I18n.get("gui.charm.compass.coords", pos.getX(), pos.getY(), pos.getZ());
        }

        var coordsLength = font.width(coords);
        var coordsColor = 0xAA9988;
        var facing = I18n.get("gui.charm.compass.facing", direction.getName());
        var facingLength = font.width(facing);
        var facingColor = 0xFFEEDD;
        float midX;

        if (feature().compactView()) {
            midX = window.getGuiScaledWidth() / 9.0F;
            y = 10;
            lineHeight = 9;
        } else {
            midX = window.getGuiScaledWidth() / 2.0F;
            y = 40;
            lineHeight = 12;
        }

        if (feature().showFacing()) {
            renderText(guiGraphics, font, facing, midX, y, -facingLength / 2, 0, facingColor | alpha);
            y += lineHeight;
        }

        if (feature().showCoords()) {
            renderText(guiGraphics, font, coords, midX, y, -coordsLength / 2, 0, coordsColor | alpha);
            y += lineHeight;
        }

        if (feature().showBiome()) {
            var biomeRes = player.level().getBiome(pos).unwrap().map(key -> key != null ? key.location() : null, unknown -> null);
            if (biomeRes != null) {
                var biomeName = I18n.get("biome." + biomeRes.getNamespace() + "." + biomeRes.getPath());
                var biomeText = Component.translatable("gui.charm.compass.biome", biomeName).getString();
                var biomeLength = font.width(biomeText);
                var biomeColor = 0x9ACCAA;
                renderText(guiGraphics, font, biomeText, midX, y, -biomeLength / 2, 0, biomeColor | alpha);
                y += lineHeight;
            }
        }

        for (var item : feature().providers.overlayItems) {
            if (!item.shouldShow(player)) continue;
            var length = font.width(item.text());
            renderText(guiGraphics, font, item.text(), midX, y, -length / 2, 0, item.color() | alpha);
            y += lineHeight;
        }
    }

    @SuppressWarnings("SameParameterValue")
    public void renderText(GuiGraphics guiGraphics, Font font, String text, float translateX, float translateY, int offsetX, int offsetY, int color) {
        var pose = guiGraphics.pose();
        var useX = offsetX;

        pose.pushPose();
        pose.translate(translateX, translateY, 0.0);

        if (feature().compactView()) {
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
