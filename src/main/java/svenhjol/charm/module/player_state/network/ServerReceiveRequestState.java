package svenhjol.charm.module.player_state.network;

import net.minecraft.core.BlockPos;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import svenhjol.charm.helper.WorldHelper;
import svenhjol.charm.module.player_state.PlayerState;
import svenhjol.charm.network.Id;
import svenhjol.charm.network.ServerReceiver;

import java.util.Map;

@Id("charm:request_player_state")
public class ServerReceiveRequestState extends ServerReceiver {
    @Override
    public void handle(MinecraftServer server, ServerPlayer player, FriendlyByteBuf buffer) {
        ServerLevel level = player.getLevel();
        BlockPos pos = player.blockPosition();
        CompoundTag tag = new CompoundTag();

        // Check if player is in a structure bounds and if so add it to a structure list.
        ListTag withinStructures = new ListTag();

        var entries = BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE.entrySet();
        for (Map.Entry<ResourceKey<ConfiguredStructureFeature<?, ?>>, ConfiguredStructureFeature<?, ?>> entry : entries) {
            var key = entry.getKey();
            ConfiguredStructureFeature<?, ?> structureFeature = entry.getValue();

            if (WorldHelper.isInsideStructure(level, pos, structureFeature)) {
                withinStructures.add(StringTag.valueOf(key.location().toString()));
            }
        }

        tag.put(PlayerState.WITHIN_STRUCTURES_TAG, withinStructures);

        // Allow other mods to add to the tag.
        PlayerState.CALLBACKS.forEach(action -> action.accept(player, tag));

        // Send the tag to the client.
        PlayerState.SERVER_SEND_STATE.send(player, tag);
    }

    @Override
    protected boolean showDebugMessages() {
        return false;
    }
}
