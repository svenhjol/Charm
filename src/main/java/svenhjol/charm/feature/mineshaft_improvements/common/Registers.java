package svenhjol.charm.feature.mineshaft_improvements.common;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import svenhjol.charm.Charm;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.mineshaft_improvements.MineshaftImprovements;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Registers extends RegisterHolder<MineshaftImprovements> {
    // Holds loot tables that will be used to populate custom minecarts.
    public final List<ResourceLocation> minecartLoot = new ArrayList<>();

    static final List<BlockState> FLOOR_BLOCKS = new ArrayList<>();
    static final List<BlockState> CEILING_BLOCKS = new ArrayList<>();
    static final List<BlockState> PILE_BLOCKS = new ArrayList<>();
    static final List<BlockState> ROOM_BLOCKS = new ArrayList<>();
    static final List<BlockState> ROOM_DECORATIONS = new ArrayList<>();

    static final ResourceLocation FLOOR_BLOCK_LOOT = new ResourceLocation(Charm.ID, "improved_mineshafts/floor_blocks");
    static final ResourceLocation PILE_BLOCK_LOOT = new ResourceLocation(Charm.ID, "improved_mineshafts/pile_blocks");
    static final ResourceLocation CEILING_BLOCK_LOOT = new ResourceLocation(Charm.ID, "improved_mineshafts/ceiling_blocks");
    static final ResourceLocation ROOM_BLOCK_LOOT = new ResourceLocation(Charm.ID, "improved_mineshafts/room_blocks");
    static final ResourceLocation ROOM_DECORATION_LOOT = new ResourceLocation(Charm.ID, "improved_mineshafts/room_decorations");

    public Registers(MineshaftImprovements feature) {
        super(feature);

        minecartLoot.addAll(feature().minecartLoot().stream().map(ResourceLocation::new).toList());
    }

    @Override
    public void onWorldLoaded(MinecraftServer server, ServerLevel level) {
        var builder = new LootParams.Builder(level);

        FLOOR_BLOCKS.addAll(parseLootTable(server, builder, FLOOR_BLOCK_LOOT));
        CEILING_BLOCKS.addAll(parseLootTable(server, builder, PILE_BLOCK_LOOT));
        PILE_BLOCKS.addAll(parseLootTable(server, builder, CEILING_BLOCK_LOOT));
        ROOM_BLOCKS.addAll(parseLootTable(server, builder, ROOM_BLOCK_LOOT));
        ROOM_DECORATIONS.addAll(parseLootTable(server, builder, ROOM_DECORATION_LOOT));
    }

    private List<BlockState> parseLootTable(MinecraftServer server, LootParams.Builder builder, ResourceLocation tableName) {
        var lootTable = server.getLootData().getLootTable(tableName);
        var items = lootTable.getRandomItems(builder.create(LootContextParamSets.EMPTY));
        List<BlockState> states = new ArrayList<>();

        for (ItemStack stack : items) {
            BlockState state = getBlockStateFromItemStack(stack);
            states.add(state);
        }

        return states;
    }

    private BlockState getBlockStateFromItemStack(ItemStack stack) {
        var block = Block.byItem(stack.getItem());
        var state = block.defaultBlockState();
        var tag = stack.getTag();

        if (tag != null) {
            var definition = block.getStateDefinition();
            var blockStateTag = tag.getCompound(BlockItem.BLOCK_STATE_TAG);

            for (String key : blockStateTag.getAllKeys()) {
                var prop = definition.getProperty(key);
                if (prop == null) continue;
                var propString = Objects.requireNonNull(blockStateTag.get(key)).getAsString();
                state = BlockItem.updateState(state, prop, propString);
            }
        }

        return state;
    }
}
