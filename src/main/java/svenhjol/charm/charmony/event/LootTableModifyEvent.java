package svenhjol.charm.charmony.event;

import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.Optional;

@SuppressWarnings("unused")
public class LootTableModifyEvent extends CharmEvent<LootTableModifyEvent.Handler> {
    public static final LootTableModifyEvent INSTANCE = new LootTableModifyEvent();

    private LootTableModifyEvent() {}

    public void invoke(ResourceLocation tableId, LootTableSource source, LootDataManager manager, LootTable.Builder builder) {
        for (var handler : getHandlers()) {
            handler.run(tableId, source, manager).ifPresent(pool -> builder.pool(pool.build()));
        }
    }

    @FunctionalInterface
    public interface Handler {
        Optional<LootPool.Builder> run(ResourceLocation tableId, LootTableSource source, LootDataManager manager);
    }
}
