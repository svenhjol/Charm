package svenhjol.charm.feature.spawners_drop_items;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import svenhjol.charm.feature.spawners_drop_items.common.Handlers;
import svenhjol.charm.feature.spawners_drop_items.common.Registers;
import svenhjol.charm.foundation.Resolve;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = """
    Monster spawners drop mob-related items when broken. This allows items such as gunpowder, string
    and rotten flesh to be gathered in larger quantities when the game difficulty is set to peaceful.""")
public final class SpawnersDropItems extends CommonFeature {
    public final Registers registers;
    public final Handlers handlers;

    @Configurable(
        name = "Peaceful only",
        description = """
            If true, monster spawners only drop items when the game difficulty is set to peaceful.
            If false, monster spawners drop items regardless of the game difficulty.""")
    private static boolean onlyPeaceful = true;

    public SpawnersDropItems(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
    }

    public boolean onlyPeaceful() {
        return onlyPeaceful;
    }

    /**
     * Helper method to register new spawner drops.
     * TODO: create API interface for this.
     */
    @Deprecated
    public static void registerDropType(TagKey<EntityType<?>> entity, Item item, int amount) {
        Resolve.feature(SpawnersDropItems.class).registers.registerDropType(entity, item, amount);
    }
}
