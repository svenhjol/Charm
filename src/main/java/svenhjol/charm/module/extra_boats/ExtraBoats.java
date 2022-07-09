package svenhjol.charm.module.extra_boats;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.BoatItem;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.module.variant_chests.VariantChests;
import svenhjol.charm.registry.CommonRegistry;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

@CommonModule(mod = Charm.MOD_ID, priority = 5, alwaysEnabled = true, description = "Allows Charm to register extra boat types.")
public class ExtraBoats extends CharmModule {
    private static final Map<Boat.Type, BoatItem> BOAT_TYPE_TO_BOAT = new HashMap<>();
    private static final Map<Boat.Type, BoatItem> BOAT_TYPE_TO_CHEST_BOAT = new HashMap<>();

    public static void registerBoat(ResourceLocation id, Boat.Type type, BoatItem boat, BoatItem chestBoat) {
        var namespace = id.getNamespace();
        var path = id.getPath();
        CommonRegistry.item(new ResourceLocation(namespace, path + "_boat"), boat);
        CommonRegistry.item(new ResourceLocation(namespace, path + "_chest_boat"), chestBoat);
        BOAT_TYPE_TO_BOAT.put(type, boat);
        BOAT_TYPE_TO_CHEST_BOAT.put(type, chestBoat);
    }

    @Override
    public void runWhenEnabled() {
        for (Boat.Type type : BOAT_TYPE_TO_BOAT.keySet()) {
            var boat = BOAT_TYPE_TO_BOAT.get(type);
            var chestBoat = BOAT_TYPE_TO_CHEST_BOAT.get(type);

            if (boat != null && chestBoat != null) {
                VariantChests.CHEST_BOATS.put(boat, chestBoat);
            }
        }
    }

    @Nullable
    public static BoatItem getBoatByType(Boat.Type type) {
        return BOAT_TYPE_TO_BOAT.getOrDefault(type, null);
    }

    @Nullable
    public static BoatItem getChestBoatByType(Boat.Type type) {
        return BOAT_TYPE_TO_CHEST_BOAT.getOrDefault(type, null);
    }
}
