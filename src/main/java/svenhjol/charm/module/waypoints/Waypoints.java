package svenhjol.charm.module.waypoints;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.player.Player;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.api.event.PlayerTickCallback;
import svenhjol.charm.helper.TextHelper;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.module.waypoints.network.ServerSendClosestWaypoint;
import svenhjol.charm.module.waypoints.network.ServerSendFlushWaypoint;
import svenhjol.charm.registry.CommonRegistry;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@CommonModule(mod = Charm.MOD_ID, description = "Lodestones broadcast a message to a nearby player.\n" +
    "Place a named banner on a lodestone to start broadcasting.")
public class Waypoints extends CharmModule {
    public static boolean useDropShadow = false;
    public static boolean flushLastWaypoint = true;
    public static int broadcastDuration = 12;
    public static int broadcastDistance = 128;
    public static SoundEvent BROADCAST_SOUND;
    public static ServerSendClosestWaypoint SEND_CLOSEST_WAYPOINT;
    public static ServerSendFlushWaypoint SEND_FLUSH_WAYPOINT;
    public static Map<UUID, String> LAST_SEEN_WAYPOINT = new HashMap<>();
    public static final int MIN_BACKOFF_TICKS = 20;
    public static final int MAX_BACKOFF_TICKS = 50;
    protected int ticksBackoff = MIN_BACKOFF_TICKS;

    @Override
    public void register() {
        BROADCAST_SOUND = CommonRegistry.sound(new ResourceLocation(Charm.MOD_ID, "waypoint_broadcasting"));
    }

    @Override
    public void runWhenEnabled() {
        PlayerTickCallback.EVENT.register(this::handlePlayerTick);
        SEND_CLOSEST_WAYPOINT = new ServerSendClosestWaypoint();
        SEND_FLUSH_WAYPOINT = new ServerSendFlushWaypoint();
    }

    @Nullable
    public static BlockPos getClosest(ServerLevel level, BlockPos pos) {
        var poiManager = level.getPoiManager();
        var closest = poiManager.findClosest(holder -> holder.is(PoiTypes.LODESTONE), pos, broadcastDistance, PoiManager.Occupancy.ANY);
        return closest.orElse(null);
    }

    private void handlePlayerTick(Player player) {
        if (player.level.isClientSide) return;
        if (player.level.getGameTime() % ticksBackoff != 0) return;

        var serverPlayer = (ServerPlayer) player;
        var playerPos = serverPlayer.blockPosition();
        var uuid = player.getUUID();
        var level = serverPlayer.getLevel();
        var lodestonePos = getClosest(level, playerPos);

        if (lodestonePos == null) {
            if (flushLastWaypoint) {
                // Remove the last seen entry and send a message to the client to clear.
                LAST_SEEN_WAYPOINT.remove(uuid);
                SEND_FLUSH_WAYPOINT.send(serverPlayer);
            }
            if (ticksBackoff < MAX_BACKOFF_TICKS) {
                ticksBackoff += 5;
            }
            return;
        }

        ticksBackoff = MIN_BACKOFF_TICKS;

        var hash = WaypointHelper.getHash(level, lodestonePos);
        if (hash == null) return;

        var banner = WaypointHelper.getLodestoneBanner(level, lodestonePos);
        if (banner == null) return;

        var shouldPlaySound = WaypointHelper.shouldPlaySound(level, lodestonePos);
        var lastHash = LAST_SEEN_WAYPOINT.getOrDefault(uuid, "");

        // Already seen this, return.
        if (hash.equals(lastHash)) return;

        // Not seen, send message to client to show the title.
        LAST_SEEN_WAYPOINT.put(uuid, hash);

        var title = banner.getDisplayName();
        var color = banner.getBaseColor();

        // Bit of a hack to get banners to display something when unnamed.
        if (title.getString().equals("block.minecraft.banner")) {
            title = TextHelper.translatable("block.minecraft." + color.getSerializedName() + "_banner");
        }

        SEND_CLOSEST_WAYPOINT.send(serverPlayer, title, color, shouldPlaySound);
    }
}
