package svenhjol.charm.module;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.RegistryHandler;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.base.item.CharmBoatItem;
import svenhjol.charm.client.ExtraBoatsClient;
import svenhjol.charm.entity.CharmBoatEntity;
import svenhjol.charm.item.CrimsonBoatItem;
import svenhjol.charm.item.WarpedBoatItem;

@Module(mod = Charm.MOD_ID, priority = 5, client = ExtraBoatsClient.class, description = "Adds crimson and warped wood boats.")
public class ExtraBoats extends CharmModule {
    public static EntityType<CharmBoatEntity> CHARM_BOAT;

    public static CharmBoatItem CRIMSON_BOAT;
    public static CharmBoatItem WARPED_BOAT;

    @Override
    public void register() {
        CHARM_BOAT = RegistryHandler.entity(new Identifier(Charm.MOD_ID, "boat"), FabricEntityTypeBuilder
            .<CharmBoatEntity>create(SpawnGroup.MISC, CharmBoatEntity::new)
            .dimensions(EntityDimensions.fixed(1.375F, 0.5625F))
            .trackRangeBlocks(10));

        CRIMSON_BOAT = new CrimsonBoatItem(this);
        WARPED_BOAT = new WarpedBoatItem(this);
    }
}
