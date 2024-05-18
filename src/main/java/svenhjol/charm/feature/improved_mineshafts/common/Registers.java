package svenhjol.charm.feature.improved_mineshafts.common;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import svenhjol.charm.api.event.LevelLoadEvent;
import svenhjol.charm.feature.improved_mineshafts.ImprovedMineshafts;
import svenhjol.charm.foundation.feature.RegisterHolder;

import java.util.ArrayList;
import java.util.List;

public final class Registers extends RegisterHolder<ImprovedMineshafts> {
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

    public Registers(ImprovedMineshafts feature) {
        super(feature);
        var registry = feature.registry();

        minecartLoot.addAll(ImprovedMineshafts.minecartLoot.stream().map(registry::lootTable).toList());
        floorBlockLoot = registry.lootTable("improved_mineshafts/floor_blocks");
        pileBlockLoot = registry.lootTable("improved_mineshafts/pile_blocks");
        ceilingBlockLoot = registry.lootTable("improved_mineshafts/ceiling_blocks");
        roomBlockLoot = registry.lootTable("improved_mineshafts/room_blocks");
        roomDecorationLoot = registry.lootTable("improved_mineshafts/room_decorations");
    }

    @Override
    public void onEnabled() {
        LevelLoadEvent.INSTANCE.handle(feature().handlers::levelLoad);
    }
}
