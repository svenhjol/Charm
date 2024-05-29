package svenhjol.charm.feature.mineshaft_improvements.common;

import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.mineshaft_improvements.MineshaftImprovements;

import java.util.ArrayList;
import java.util.List;

public final class Registers extends RegisterHolder<MineshaftImprovements> {
    // Holds loot tables that will be used to populate custom minecarts.
    public final List<ResourceKey<LootTable>> minecartLoot = new ArrayList<>();

    public final ResourceKey<LootTable> floorBlockLoot;
    public final ResourceKey<LootTable> pileBlockLoot;
    public final ResourceKey<LootTable> ceilingBlockLoot;
    public final ResourceKey<LootTable> roomBlockLoot;
    public final ResourceKey<LootTable> roomDecorationLoot;

    public final List<BlockState> blocksForFloor = new ArrayList<>();
    public final List<BlockState> blocksForCeiling = new ArrayList<>();
    public final List<BlockState> blocksForPile = new ArrayList<>();
    public final List<BlockState> blocksForRoom = new ArrayList<>();
    public final List<BlockState> decorationsForRoom = new ArrayList<>();

    public Registers(MineshaftImprovements feature) {
        super(feature);
        var registry = feature.registry();

        minecartLoot.addAll(feature().minecartLoot().stream().map(registry::lootTable).toList());
        floorBlockLoot = registry.lootTable("mineshaft_improvements/floor_blocks");
        pileBlockLoot = registry.lootTable("mineshaft_improvements/pile_blocks");
        ceilingBlockLoot = registry.lootTable("mineshaft_improvements/ceiling_blocks");
        roomBlockLoot = registry.lootTable("mineshaft_improvements/room_blocks");
        roomDecorationLoot = registry.lootTable("mineshaft_improvements/room_decorations");
    }

    @Override
    public void onWorldLoaded(MinecraftServer server, ServerLevel level) {
        var builder = new LootParams.Builder(level);

        blocksForCeiling.clear();
        blocksForRoom.clear();
        blocksForPile.clear();
        blocksForFloor.clear();
        decorationsForRoom.clear();

        blocksForFloor.addAll(parseLootTable(server, builder, floorBlockLoot));
        blocksForPile.addAll(parseLootTable(server, builder, pileBlockLoot));
        blocksForCeiling.addAll(parseLootTable(server, builder, ceilingBlockLoot));
        blocksForRoom.addAll(parseLootTable(server, builder, roomBlockLoot));
        decorationsForRoom.addAll(parseLootTable(server, builder, roomDecorationLoot));
    }

    private List<BlockState> parseLootTable(MinecraftServer server, LootParams.Builder builder, ResourceKey<LootTable> tableName) {
        var lootTable = server.reloadableRegistries().getLootTable(tableName);
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
        var data = stack.get(DataComponents.BLOCK_STATE);

        if (data != null) {
            return data.apply(state);
        }

        return state;
    }
}
