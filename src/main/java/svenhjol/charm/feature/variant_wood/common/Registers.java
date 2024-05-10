package svenhjol.charm.feature.variant_wood.common;

import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.api.CharmApi;
import svenhjol.charm.api.event.EntityUseEvent;
import svenhjol.charm.api.iface.IVariantMaterial;
import svenhjol.charm.feature.variant_wood.VariantWood;
import svenhjol.charm.feature.variant_wood.block.entity.ChestBlockEntity;
import svenhjol.charm.feature.variant_wood.block.entity.TrappedChestBlockEntity;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.feature.RegisterHolder;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class Registers extends RegisterHolder<VariantWood> {
    public final Map<IVariantMaterial, VariantBarrel> barrels = new LinkedHashMap<>();
    public final Map<IVariantMaterial, VariantBookshelf> bookshelves = new LinkedHashMap<>();
    public final Map<IVariantMaterial, VariantChest> chests = new LinkedHashMap<>();
    public final Map<IVariantMaterial, VariantChiseledBookshelf> chiseledBookshelves = new LinkedHashMap<>();
    public final Map<IVariantMaterial, VariantLadder> ladders = new LinkedHashMap<>();
    public final Map<IVariantMaterial, VariantTrappedChest> trappedChests = new LinkedHashMap<>();
    public final Supplier<BlockEntityType<ChestBlockEntity>> chestBlockEntity;
    public final Supplier<BlockEntityType<TrappedChestBlockEntity>> trappedChestBlockEntity;

    public Registers(VariantWood feature) {
        super(feature);

        // Hack to inject the boat type enums early.
        var boatTypeValues = Boat.Type.values();

        chestBlockEntity = feature.registry().blockEntity("variant_chest", () -> ChestBlockEntity::new);
        trappedChestBlockEntity = feature.registry().blockEntity("variant_trapped_chest", () -> TrappedChestBlockEntity::new);

        CharmApi.registerProvider(new DataProviders(feature));
    }

    public void register(CommonFeature feature, IVariantMaterial material) {
        barrels.put(material, new VariantBarrel(feature, material));
        bookshelves.put(material, new VariantBookshelf(feature, material));
        chests.put(material, new VariantChest(feature, material));
        chiseledBookshelves.put(material, new VariantChiseledBookshelf(feature, material));
        ladders.put(material, new VariantLadder(feature, material));
        trappedChests.put(material, new VariantTrappedChest(feature, material));
    }

    @Override
    public void onEnabled() {
        EntityUseEvent.INSTANCE.handle(feature().handlers::animalInteraction);
    }
}
