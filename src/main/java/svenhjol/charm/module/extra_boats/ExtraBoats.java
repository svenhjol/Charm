package svenhjol.charm.module.extra_boats;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.helper.RegistryHelper;
import svenhjol.charm.item.CharmBoatItem;
import svenhjol.charm.loader.CharmCommonModule;

@CommonModule(mod = Charm.MOD_ID, priority = 5, description = "Adds crimson and warped wood boats.")
public class ExtraBoats extends CharmCommonModule {
    public static EntityType<CharmBoatEntity> CHARM_BOAT;

    public static CharmBoatItem CRIMSON_BOAT;
    public static CharmBoatItem WARPED_BOAT;

    @Override
    public void register() {
        CHARM_BOAT = RegistryHelper.entity(new ResourceLocation(Charm.MOD_ID, "boat"), FabricEntityTypeBuilder
            .<CharmBoatEntity>create(MobCategory.MISC, CharmBoatEntity::new)
            .dimensions(EntityDimensions.fixed(1.375F, 0.5625F))
            .trackRangeBlocks(10));

        CRIMSON_BOAT = new CrimsonBoatItem(this);
        WARPED_BOAT = new WarpedBoatItem(this);
    }
}
