package svenhjol.charm.feature.custom_wood.common;

import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.api.event.EntityUseEvent;
import svenhjol.charm.api.event.LevelLoadEvent;
import svenhjol.charm.api.iface.IVariantWoodMaterial;
import svenhjol.charm.feature.custom_wood.CustomWood;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonRegistry;
import svenhjol.charm.foundation.feature.RegisterHolder;

import java.util.HashMap;
import java.util.Map;

public final class Registers extends RegisterHolder<CustomWood> {
    public final CommonRegistry.Register<BlockEntityType<ChestBlockEntity>> chestBlockEntity;
    public final CommonRegistry.Register<BlockEntityType<TrappedChestBlockEntity>> trappedChestBlockEntity;
    public final Map<IVariantWoodMaterial, CustomWoodHolder> holders = new HashMap<>();

    public Registers(CustomWood feature) {
        super(feature);

        // Hack to inject the boat type enums early.
        @SuppressWarnings("unused")
        var boatTypeValues = Boat.Type.values();

        chestBlockEntity = feature.registry().blockEntity("variant_chest", () -> ChestBlockEntity::new);
        trappedChestBlockEntity = feature.registry().blockEntity("variant_trapped_chest", () -> TrappedChestBlockEntity::new);
    }

    @Override
    public void onEnabled() {
        LevelLoadEvent.INSTANCE.handle(feature().handlers::levelLoad);
        EntityUseEvent.INSTANCE.handle(feature().handlers::animalInteraction);
    }

    public void register(CommonFeature owner, CustomWoodDefinition definition) {
        holders.put(definition.material(), new CustomWoodHolder(feature(), owner, definition));
    }
}
