package svenhjol.charm.module.compass_overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.Items;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.loader.CharmModule;

@SuppressWarnings("SameParameterValue")
@ClientModule(module = CompassOverlay.class)
public class CompassOverlayClient extends CharmModule {
    @Override
    public void runWhenEnabled() {
        HudRenderCallback.EVENT.register(this::handleHudRender);
    }

    private void handleHudRender(PoseStack poseStack, float tickDelta) {
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

        if (CompassOverlay.showFacing) {
            renderText(poseStack, font, facing, gui.screenWidth / 2.0F, y, -facingLength / 2.0F, 0.0F, facingColor | alpha);
            y += 12;
        }

        if (CompassOverlay.showCoords) {
            renderText(poseStack, font, coords, gui.screenWidth / 2.0F, y, -coordsLength / 2.0F, 0.0F, coordsColor | alpha);
        }
    }

    private void renderText(PoseStack poseStack, Font font, String text, float translateX, float translateY, float offsetX, float offsetY, int color) {
        poseStack.pushPose();
        poseStack.translate(translateX, translateY, 0.0);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        font.draw(poseStack, text, offsetX, offsetY, color);
        RenderSystem.disableBlend();
        poseStack.popPose();
    }
}
