package svenhjol.charm.module.player_state;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.api.CharmNetworkReferences;
import svenhjol.charm.api.CharmPlayerStateKeys;
import svenhjol.charm.helper.NetworkHelper;
import svenhjol.charm.helper.PosHelper;
import svenhjol.charm.loader.CharmModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@SuppressWarnings("unused")
@CommonModule(mod = Charm.MOD_ID, alwaysEnabled = true, description = "Synchronize additional state from server to client.")
public class PlayerState extends CharmModule {
    public static final ResourceLocation MSG_SERVER_UPDATE = new ResourceLocation(CharmNetworkReferences.ServerUpdatePlayerState.toString());
    private static final Map<String, StructureFeature<?>> VANILLA_STRUCTURES = new HashMap<>();
    private static final List<String> OVERWORLD_RUINS = new ArrayList<>();
    private static final List<String> NETHER_RUINS = new ArrayList<>();
    private static final List<String> END_RUINS = new ArrayList<>();
    private static final List<BiConsumer<ServerPlayer, CompoundTag>> callbacks = new ArrayList<>();

    @Config(name = "Server state update interval", description = "Interval (in ticks) on which additional player state will be synchronised to the client.")
    public static int heartbeat = 120;

    @Override
    public void register() {
        // when server_update request received from the client, prepare the state to send back to the client
        ServerPlayNetworking.registerGlobalReceiver(MSG_SERVER_UPDATE, this::handleUpdatePlayerState);

        // set up vanilla structures to test if player is inside them
        initVanillaStructures();
    }

    public static void addCallback(BiConsumer<ServerPlayer, CompoundTag> callback) {
        callbacks.add(callback);
    }

    private void handleUpdatePlayerState(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf data, PacketSender sender) {
        server.execute(() -> serverCallback(player));
    }

    /**
     * Populates an NBT tag of state information about the player.
     * Sends a compressed string of data to the client to unpack.
     */
    private void serverCallback(ServerPlayer player) {
        ServerLevel level = player.getLevel();
        BlockPos pos = player.blockPosition();
        CompoundTag nbt = new CompoundTag();

        // if the player is inside a vanilla structure, add it to the nbt
        for (Map.Entry<String, StructureFeature<?>> entry : VANILLA_STRUCTURES.entrySet()) {
            if (PosHelper.isInsideStructure(level, pos, entry.getValue())) {
                String key = entry.getKey();
                nbt.putBoolean(key, true);

                if (OVERWORLD_RUINS.contains(key))
                    nbt.putBoolean(CharmPlayerStateKeys.InsideOverworldRuin.toString(), true);

                if (NETHER_RUINS.contains(key))
                    nbt.putBoolean(CharmPlayerStateKeys.InsideNetherRuin.toString(), true);

                break;
            }
        }

        // allow other mods to update the nbt
        callbacks.forEach(action -> action.accept(player, nbt));

        // send updated player data to client
        FriendlyByteBuf buffer = NetworkHelper.encodeNbt(nbt);

        if (buffer != null)
            ServerPlayNetworking.send(player, PlayerStateClient.MSG_CLIENT_UPDATE, buffer);
    }

    private void initVanillaStructures() {
        VANILLA_STRUCTURES.put(CharmPlayerStateKeys.InsideVillage.toString(), StructureFeature.VILLAGE);
        VANILLA_STRUCTURES.put(CharmPlayerStateKeys.InsideMineshaft.toString(), StructureFeature.MINESHAFT);
        VANILLA_STRUCTURES.put(CharmPlayerStateKeys.InsideStronghold.toString(), StructureFeature.STRONGHOLD);
        VANILLA_STRUCTURES.put(CharmPlayerStateKeys.InsideDesertPyramid.toString(), StructureFeature.DESERT_PYRAMID);
        VANILLA_STRUCTURES.put(CharmPlayerStateKeys.InsideJunglePyramid.toString(), StructureFeature.JUNGLE_TEMPLE);
        VANILLA_STRUCTURES.put(CharmPlayerStateKeys.InsideIgloo.toString(), StructureFeature.IGLOO);
        VANILLA_STRUCTURES.put(CharmPlayerStateKeys.InsideMansion.toString(), StructureFeature.WOODLAND_MANSION);
        VANILLA_STRUCTURES.put(CharmPlayerStateKeys.InsideDesertPyramid.toString(), StructureFeature.DESERT_PYRAMID);
        VANILLA_STRUCTURES.put(CharmPlayerStateKeys.InsideSwampHut.toString(), StructureFeature.SWAMP_HUT);
        VANILLA_STRUCTURES.put(CharmPlayerStateKeys.InsideOceanMonument.toString(), StructureFeature.OCEAN_MONUMENT);
        VANILLA_STRUCTURES.put(CharmPlayerStateKeys.InsideOceanRuin.toString(), StructureFeature.OCEAN_RUIN);
        VANILLA_STRUCTURES.put(CharmPlayerStateKeys.InsideNetherFortress.toString(), StructureFeature.NETHER_BRIDGE);
        VANILLA_STRUCTURES.put(CharmPlayerStateKeys.InsideBastionRemnant.toString(), StructureFeature.BASTION_REMNANT);
        VANILLA_STRUCTURES.put(CharmPlayerStateKeys.InsideEndCity.toString(), StructureFeature.END_CITY);

        OVERWORLD_RUINS.add(CharmPlayerStateKeys.InsideStronghold.toString());
        NETHER_RUINS.add(CharmPlayerStateKeys.InsideBastionRemnant.toString());
    }
}
