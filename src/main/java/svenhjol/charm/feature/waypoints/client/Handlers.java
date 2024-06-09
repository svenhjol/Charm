package svenhjol.charm.feature.waypoints.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import svenhjol.charm.charmony.feature.FeatureHolder;
import svenhjol.charm.feature.waypoints.WaypointsClient;
import svenhjol.charm.feature.waypoints.common.Networking;

import java.util.List;
import java.util.Optional;

public final class Handlers extends FeatureHolder<WaypointsClient> {
    public static final String STRENGTH_ICON = "⭐";
    public static final List<DyeColor> COLORS_WITH_HIGH_BRIGHTNESS;
    public static final List<DyeColor> COLORS_WITH_LOW_BRIGHTNESS;
    public static final List<DyeColor> COLORS_WITH_BRIGHT_BACKGROUND;

    private DyeColor lastSeenColor; // Cached color of the last message.
    private String lastSeenTitle; // Cached title of the last message.
    private BlockPos lastSeenPos; // Cached target pos of the last message.
    private Component broadcastMessage = null; // Display message component when this is not null.
    private int broadcastBackground = 0; // Color for the background of the displayed message.
    private int broadcastTime = 0; // Number of ticks that the message component has been shown.

    public Handlers(WaypointsClient feature) {
        super(feature);
    }

    public boolean isPlayerInRange() {
        return lastSeenTitle != null; // Title of the waypoint will remain until S2CClearWaypointInfo packet resets it.
    }

    public Optional<String> title() {
        return Optional.ofNullable(lastSeenTitle);
    }

    public Optional<BlockPos> pos() {
        return Optional.ofNullable(lastSeenPos);
    }

    public Optional<DyeColor> color() {
        return Optional.ofNullable(lastSeenColor);
    }

    public String strengthMeter() {
        var minecraft = Minecraft.getInstance();
        if (minecraft.player == null || lastSeenPos == null) return "";

        var playerPos = minecraft.player.blockPosition();
        var maxDistance = feature().linked().broadcastDistance();
        var currentDistance = Mth.clamp(playerPos.distManhattan(lastSeenPos), 1, maxDistance);

        var i = 0d;
        var pc = 100d - ((double)currentDistance / (double)maxDistance * 100d);
        var sb = new StringBuilder(STRENGTH_ICON);

        while (i < pc) {
            sb.append(STRENGTH_ICON);
            i += 25d;
        }

        return sb.toString();
    }

    public void updateWaypointInfoReceived(Player player, Networking.S2CUpdateWaypointInfo packet) {
        var random = player.getRandom();

        // Prevent spamming the client if the message and color are the same.
        var pos = packet.pos();
        var color = packet.color();
        var playSound = packet.playSound();
        var title = packet.title();
        var titleAsString = title.getString();

        if (lastSeenTitle != null
            && lastSeenColor != null
            && lastSeenPos != null
            && lastSeenTitle.equals(titleAsString)
            && lastSeenColor.equals(color)
            && lastSeenPos.equals(pos)
        ) {
            return; // Don't spam if it's the same thing the client has already seen.
        }

        lastSeenColor = color;
        lastSeenTitle = titleAsString;
        lastSeenPos = pos;

        var displayTitle = (MutableComponent)title;

        if (playSound) {
            Minecraft.getInstance().getSoundManager().play(
                SimpleSoundInstance.forUI(feature().linked().registers.broadcastSound.get(), 0.9f + (0.2f * random.nextFloat()), 0.35f));
        }
        
        int textColor;
        int backgroundColor;
        
        if (COLORS_WITH_LOW_BRIGHTNESS.contains(color)) {
            textColor = color.getTextColor() & 0xe0e0e0;
        } else if (COLORS_WITH_HIGH_BRIGHTNESS.contains(color)) {
            textColor = color.getTextColor() | 0x303030;
        } else {
            textColor = color.getTextColor();
        }
        
        if (COLORS_WITH_BRIGHT_BACKGROUND.contains(color)) {
            backgroundColor = 0xffffff;
        } else {
            backgroundColor = 0x080808;
        }
        
        broadcastMessage = displayTitle.withStyle(style -> style.withColor(textColor));
        broadcastBackground = backgroundColor;
        broadcastTime = feature().linked().messageDuration() * 60;
    }

    @SuppressWarnings("unused")
    public void clearWaypointInfoReceived(Player player, Networking.S2CClearWaypointInfo packet) {
        lastSeenColor = null;
        lastSeenTitle = null;
        lastSeenPos = null;
    }

    public void hudRender(GuiGraphics guiGraphics, DeltaTracker tickDelta) {
        if (broadcastMessage == null || broadcastTime == 0) return;

        var minecraft = Minecraft.getInstance();

        // Don't show if Hide GUI is enabled.
        if (minecraft.options.hideGui) {
            return;
        }
        
        var window = minecraft.getWindow();
        var gui = minecraft.gui;
        var pose = guiGraphics.pose();
        var font = gui.getFont();
        var fade = broadcastTime - tickDelta.getGameTimeDeltaTicks();
        var foregroundAlpha = 0xffffffff;
        var backgroundAlpha = 0xffffffff;
        var len = 0;
        var opacity = (int)(fade * 255.0f / 80.0f);

        if (opacity > 255) {
            opacity = 255;
        }

        if (opacity > 8) {
            pose.pushPose();
            var midX = window.getGuiScaledWidth() / 2.0f;
            pose.translate(midX, 20, 0.0d);

            len = font.width(broadcastMessage);
            if (len < 200) {
                pose.scale(1.4f, 1.4f, 1.4f);
            }

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            foregroundAlpha = opacity << 24 & 0xff000000;
            backgroundAlpha = (opacity / 2) << 24 & 0xff000000;

            var x = -len / 2;
            var y = -4;
            var foreground = 0xffffff | foregroundAlpha;
            var background = broadcastBackground | backgroundAlpha;

            int n = 3;
            guiGraphics.fill(x - n, y - n, x + len + n - 1, y + 8 + n, background);
            guiGraphics.drawString(font, broadcastMessage, x, y, foreground, false);

            RenderSystem.disableBlend();
            pose.popPose();
        }

        // If still in range of waypoint and configured to keep the message showing, don't fade the message.
        if (feature().linked().messageRemainsWhileInRange() && isPlayerInRange()) {
            return;
        }

        // Broadcast ticks are used to control the fadeout.
        // Reduce broadcast ticks and hide the message once it's run out.
        broadcastTime--;
        if (broadcastTime <= 0) {
            broadcastTime = 0;
            broadcastMessage = null;
        }
    }
    
    static {
        COLORS_WITH_HIGH_BRIGHTNESS = List.of(
            DyeColor.RED, DyeColor.ORANGE, DyeColor.YELLOW, DyeColor.LIME,
            DyeColor.GREEN, DyeColor.CYAN, DyeColor.LIGHT_BLUE, DyeColor.BLUE,
            DyeColor.PURPLE, DyeColor.MAGENTA, DyeColor.PINK, DyeColor.BROWN
        );
        COLORS_WITH_LOW_BRIGHTNESS = List.of(DyeColor.LIGHT_GRAY, DyeColor.GRAY);
        COLORS_WITH_BRIGHT_BACKGROUND = List.of(DyeColor.BLACK);
    }
}
