package svenhjol.charm.module.waypoints;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DyeColor;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.module.waypoints.network.ClientReceiveClosestWaypoint;
import svenhjol.charm.module.waypoints.network.ClientReceiveFlushWaypoint;

@ClientModule(module = Waypoints.class)
public class WaypointsClient extends CharmModule {
    public static ClientReceiveClosestWaypoint RECEIVE_CLOSEST_WAYPOINT;
    public static ClientReceiveFlushWaypoint RECEIVE_FLUSH_WAYPOINT;
    public static DyeColor lastColor;
    public static String lastMessage;
    public static Component broadcastMessage = null;
    public static int broadcastMessageTime = 0;

    @Override
    public void runWhenEnabled() {
        HudRenderCallback.EVENT.register(this::handleHudRender);
        ClientPlayConnectionEvents.DISCONNECT.register(this::handlePlayerDisconnect);
        RECEIVE_CLOSEST_WAYPOINT = new ClientReceiveClosestWaypoint();
        RECEIVE_FLUSH_WAYPOINT = new ClientReceiveFlushWaypoint();
    }

    private void handlePlayerDisconnect(ClientPacketListener listener, Minecraft minecraft) {
        lastMessage = null;
        lastColor = null;
        broadcastMessage = null;
        broadcastMessageTime = 0;
    }

    private void handleHudRender(PoseStack poseStack, float tickDelta) {
        if (broadcastMessage != null && broadcastMessageTime > 0) {
            var minecraft = Minecraft.getInstance();
            var gui = minecraft.gui;
            var font = gui.getFont();
            var fade = broadcastMessageTime - tickDelta;
            var col = 0xFFFFFF;
            var len = 0;
            var brightness = (int)(fade * 255.0f / 80.0f);

            if (brightness > 255) {
                brightness = 255;
            }

            if (brightness > 8) {

                poseStack.pushPose();
                poseStack.translate(gui.screenWidth / 2.0F, 20, 0.0);

                len = font.width(broadcastMessage);
                if (len < 200) {
                    poseStack.scale(1.4F, 1.4F, 1.4F);
                }

                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                col = brightness << 24 & 0xFF000000;
                gui.drawBackdrop(poseStack, font, -4, len, 0x808080 | col);

                float x = -len / 2.0F;
                float y = -4.0F;
                int color = 0xFFFFFF | col;

                if (Waypoints.useDropShadow) {
                    font.drawShadow(poseStack, broadcastMessage, x, y, color);
                } else {
                    font.draw(poseStack, broadcastMessage, x, y, color);
                }

                RenderSystem.disableBlend();
                poseStack.popPose();
            }

            broadcastMessageTime--;
            if (broadcastMessageTime <= 0) {
                broadcastMessageTime = 0;
                broadcastMessage = null;
            }
        }
    }
}
