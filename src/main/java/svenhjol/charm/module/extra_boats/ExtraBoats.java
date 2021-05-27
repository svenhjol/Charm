package svenhjol.charm.module.extra_boats;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.helper.RegistryHelper;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.item.CharmBoatItem;

@Module(mod = Charm.MOD_ID, priority = 5, client = ExtraBoatsClient.class, description = "Adds crimson and warped wood boats.")
public class ExtraBoats extends CharmModule {
    public static EntityType<CharmBoatEntity> CHARM_BOAT;

    public static CharmBoatItem CRIMSON_BOAT;
    public static CharmBoatItem WARPED_BOAT;

    @Override
    public void register() {
        CHARM_BOAT = RegistryHelper.entity(new Identifier(Charm.MOD_ID, "boat"), FabricEntityTypeBuilder
            .<CharmBoatEntity>create(SpawnGroup.MISC, CharmBoatEntity::new)
            .dimensions(EntityDimensions.fixed(1.375F, 0.5625F))
            .trackRangeBlocks(10));

        CRIMSON_BOAT = new CrimsonBoatItem(this);
        WARPED_BOAT = new WarpedBoatItem(this);
    }
}
