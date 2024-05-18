package svenhjol.charm.feature.core.custom_wood.common;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.api.event.EntityUseEvent;
import svenhjol.charm.api.iface.CustomWoodMaterial;
import svenhjol.charm.feature.core.custom_wood.CustomWood;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonRegistry.Register;
import svenhjol.charm.foundation.feature.RegisterHolder;
import svenhjol.charm.foundation.item.CharmHangingSignItem;
import svenhjol.charm.foundation.item.CharmSignItem;

import java.util.*;
import java.util.function.Supplier;

public final class Registers extends RegisterHolder<CustomWood> {
    public final Map<Boat.Type, Supplier<BoatItem>> boatItem = new HashMap<>();
    public final Map<Boat.Type, Supplier<BoatItem>> chestBoatItem = new HashMap<>();
    public final Map<Boat.Type, ResourceLocation> boatPlanks = new HashMap<>();
    public final Map<String, Map<CustomType, List<Supplier<? extends Item>>>> creativeTabItems = new HashMap<>();
    public final List<Supplier<CharmSignItem>> signItems = new ArrayList<>();
    public final List<Supplier<CharmHangingSignItem>> hangingSignItems = new ArrayList<>();
    public final Register<BlockEntityType<ChestBlockEntity>> chestBlockEntity;
    public final Register<BlockEntityType<TrappedChestBlockEntity>> trappedChestBlockEntity;
    public final Map<CustomWoodMaterial, CustomWoodHolder> holders = new HashMap<>();

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
        EntityUseEvent.INSTANCE.handle(feature().handlers::animalInteraction);
    }

    public void register(CommonFeature owner, CustomWoodDefinition definition) {
        holders.put(definition.material(), new CustomWoodHolder(feature(), owner, definition));
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onWorldLoaded(MinecraftServer server, ServerLevel level) {
        // Set each boat type's planks.
        getBoatPlanks().forEach(
            (type, id) -> BuiltInRegistries.BLOCK.getOptional(id).ifPresent(
                block -> type.planks = block));

        // Set each sign's block and item.
        getSignItems().forEach(supplier -> {
            var sign = supplier.get();
            sign.wallBlock = sign.getWallSignBlock().get();
            sign.wallBlock.item = sign;
            sign.block = sign.getSignBlock().get();
            sign.block.item = sign;
        });

        // Set each hanging sign's block and item.
        getHangingSignItems().forEach(supplier -> {
            var sign = supplier.get();
            sign.wallBlock = sign.getWallSignBlock().get();
            sign.wallBlock.item = sign;
            sign.block = sign.getHangingBlock().get();
            sign.block.item = sign;
        });
    }

    public Map<String, Map<CustomType, List<Supplier<? extends Item>>>> getCreativeTabItems() {
        return creativeTabItems;
    }

    public List<Supplier<CharmSignItem>> getSignItems() {
        return signItems;
    }

    public List<Supplier<CharmHangingSignItem>> getHangingSignItems() {
        return hangingSignItems;
    }

    public Map<Boat.Type, ResourceLocation> getBoatPlanks() {
        return boatPlanks;
    }

    public void addCreativeTabItem(String modId, CustomType customType, Supplier<? extends Item> item) {
        creativeTabItems.computeIfAbsent(modId, m -> new LinkedHashMap<>())
            .computeIfAbsent(customType, a -> new LinkedList<>()).add(item);
    }

    public void setItemForBoat(Boat.Type type, Supplier<BoatItem> item) {
        boatItem.put(type, item);
    }

    public void setItemForChestBoat(Boat.Type type, Supplier<BoatItem> item) {
        chestBoatItem.put(type, item);
    }

    public void setPlanksForBoat(Boat.Type type, ResourceLocation id) {
        boatPlanks.put(type, id);
    }

    public void addSignItem(Supplier<CharmSignItem> item) {
        if (!signItems.contains(item)) {
            signItems.add(item);
        }
    }

    public void addHangingSignItem(Supplier<CharmHangingSignItem> item) {
        if (!hangingSignItems.contains(item)) {
            hangingSignItems.add(item);
        }
    }
}
