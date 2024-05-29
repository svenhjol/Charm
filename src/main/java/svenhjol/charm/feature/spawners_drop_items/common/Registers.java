package svenhjol.charm.feature.spawners_drop_items.common;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import svenhjol.charm.charmony.event.BlockBreakEvent;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.spawners_drop_items.SpawnersDropItems;

public final class Registers extends RegisterHolder<SpawnersDropItems> {
    public final Table<TagKey<EntityType<?>>, Item, Integer> dropTypes = HashBasedTable.create();

    public Registers(SpawnersDropItems feature) {
        super(feature);

        registerDropType(Tags.SPAWNER_DROPS_BLAZE_RODS, Items.BLAZE_ROD, 32);
        registerDropType(Tags.SPAWNER_DROPS_BONES, Items.BONE, 64);
        registerDropType(Tags.SPAWNER_DROPS_GUNPOWDER, Items.GUNPOWDER, 128);
        registerDropType(Tags.SPAWNER_DROPS_MAGMA_CREAM, Items.MAGMA_CREAM, 64);
        registerDropType(Tags.SPAWNER_DROPS_ROTTEN_FLESH, Items.ROTTEN_FLESH, 64);
        registerDropType(Tags.SPAWNER_DROPS_SLIME_BALLS, Items.SLIME_BALL, 128);
        registerDropType(Tags.SPAWNER_DROPS_SPIDER_EYES, Items.SPIDER_EYE, 64);
        registerDropType(Tags.SPAWNER_DROPS_STRING, Items.STRING, 64);
    }

    @Override
    public void onEnabled() {
        BlockBreakEvent.INSTANCE.handle(feature().handlers::blockBreak);
    }

    public void registerDropType(TagKey<EntityType<?>> entity, Item item, int amount) {
        dropTypes.put(entity, item, amount);
    }
}
