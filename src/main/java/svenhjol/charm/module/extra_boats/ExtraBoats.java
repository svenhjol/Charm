package svenhjol.charm.module.extra_boats;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.enums.VanillaWoodMaterial;
import svenhjol.charm.item.CharmBoatItem;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.registry.CommonRegistry;

import java.util.LinkedHashMap;
import java.util.Map;

@CommonModule(mod = Charm.MOD_ID, priority = 5, description = "Adds crimson and warped wood boats.")
public class ExtraBoats extends CharmModule {
    public static EntityType<CharmBoatEntity> CHARM_BOAT;

    public static final String CRIMSON = VanillaWoodMaterial.CRIMSON.getSerializedName();
    public static final String WARPED = VanillaWoodMaterial.WARPED.getSerializedName();

    public static final Map<String, CharmBoatItem> BOATS = new LinkedHashMap<>();

    @Override
    public void register() {
        CHARM_BOAT = CommonRegistry.entity(new ResourceLocation(Charm.MOD_ID, "boat"), FabricEntityTypeBuilder
            .<CharmBoatEntity>create(MobCategory.MISC, CharmBoatEntity::new)
            .dimensions(EntityDimensions.fixed(1.375F, 0.5625F))
            .trackRangeBlocks(10));

        registerBoat(CRIMSON, new CrimsonBoatItem(this));
        registerBoat(WARPED, new WarpedBoatItem(this));
    }

    public static void registerBoat(String material, CharmBoatItem boat) {
        BOATS.put(material, boat);
    }
}
