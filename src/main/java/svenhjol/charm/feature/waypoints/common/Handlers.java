package svenhjol.charm.feature.waypoints.common;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import svenhjol.charm.feature.waypoints.Waypoints;
import svenhjol.charm.feature.waypoints.common.Networking.S2CClearWaypointInfo;
import svenhjol.charm.feature.waypoints.common.Networking.S2CUpdateWaypointInfo;
import svenhjol.charm.charmony.feature.FeatureHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class Handlers extends FeatureHolder<Waypoints> {
    public static final int MIN_BACKOFF_TICKS = 20;
    public static final int MAX_BACKOFF_TICKS = 80;

    private final Map<UUID, String> playerLastSeenWaypoint = new HashMap<>();
    private int backoff = MIN_BACKOFF_TICKS;

    public Handlers(Waypoints feature) {
        super(feature);
    }

    public void playerLogin(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            S2CClearWaypointInfo.send(serverPlayer);
        }
    }

    public void playerTick(Player player) {
        if (player.level().isClientSide() || player.level().getGameTime() % backoff != 0) {
            return;
        }

        var serverPlayer = (ServerPlayer)player;
        var serverLevel = (ServerLevel)serverPlayer.level();
        var pos = serverPlayer.blockPosition();
        var uuid = serverPlayer.getUUID();

        var opt = closestWaypoint(serverLevel, pos);
        if (opt.isEmpty()) {
            playerLastSeenWaypoint.remove(uuid);
            S2CClearWaypointInfo.send(serverPlayer);

            // No waypoint nearby. Increment backoff so we don't keep looking as frequently.
            if (backoff < MAX_BACKOFF_TICKS) {
                backoff += 5;
            }
            return;
        }

        // Waypoint found.
        var closest = opt.get();
        backoff = MIN_BACKOFF_TICKS; // Reset the backoff so we check more frequently.

        var hash = closest.makeHash(serverLevel);
        var lastHash = playerLastSeenWaypoint.getOrDefault(uuid, "");
        if (hash.equals(lastHash)) {
            return; // Already seen this waypoint, return early.
        }

        playerLastSeenWaypoint.put(uuid, hash);

        // Send the waypoint data to the client.
        S2CUpdateWaypointInfo.send(serverPlayer, closest);
    }

    public Optional<WaypointData> closestWaypoint(ServerLevel level, BlockPos pos) {
        var poiManager = level.getChunkSource().getPoiManager();
        var closest = poiManager.findClosest(
            holder -> holder.is(PoiTypes.LODESTONE), pos, feature().broadcastDistance(), PoiManager.Occupancy.ANY);

        if (closest.isEmpty()) {
            return Optional.empty();
        }

        var activePos = closest.get();

        // Try and get a banner from above the lodestone.
        if (!(level.getBlockEntity(activePos.above()) instanceof BannerBlockEntity banner)) {
            return Optional.empty();
        }

        var color = banner.getBaseColor();
        var title = banner.getDisplayName();

        // Bit of a hack to get banners to display something when unnamed.
        if (title.getString().equals("block.minecraft.banner")) {
            title = Component.translatable("block.minecraft." + color.getSerializedName() + "_banner");
        }

        return Optional.of(new WaypointData(activePos, title, color));
    }
}
