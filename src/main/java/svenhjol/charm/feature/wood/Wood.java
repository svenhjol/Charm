package svenhjol.charm.feature.wood;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.variant_barrels.VariantBarrels;
import svenhjol.charm.feature.variant_bookshelves.VariantBookshelves;
import svenhjol.charm.feature.variant_chest_boats.VariantChestBoats;
import svenhjol.charm.feature.variant_chests.VariantChests;
import svenhjol.charm.feature.variant_chiseled_bookshelves.VariantChiseledBookshelves;
import svenhjol.charm.feature.variant_ladders.VariantLadders;
import svenhjol.charm_api.iface.IVariantWoodMaterial;
import svenhjol.charm_core.annotation.Feature;
import svenhjol.charm_core.base.CharmFeature;
import svenhjol.charm_core.block.*;
import svenhjol.charm_core.iface.ICommonRegistry;
import svenhjol.charm_core.item.CharmBoatItem;
import svenhjol.charm_core.item.CharmHangingSignItem;
import svenhjol.charm_core.item.CharmSignItem;
import svenhjol.charm_core.mixin.accessor.BlockItemAccessor;
import svenhjol.charm_core.mixin.accessor.StandingAndWallBlockItemAccessor;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

@SuppressWarnings({"unused", "UnusedReturnValue"})
@Feature(mod = Charm.MOD_ID, canBeDisabled = false, description = "Registers custom wood types.")
public class Wood extends CharmFeature {
    private static final Map<Boat.Type, ResourceLocation> BOAT_PLANKS_IDENTIFIERS = new HashMap<>();
    private static final Map<Boat.Type, Supplier<CharmBoatItem>> TYPE_TO_BOAT = new HashMap<>();
    private static final Map<Boat.Type, Supplier<CharmBoatItem>> TYPE_TO_CHEST_BOAT = new HashMap<>();
    private static final List<Supplier<CharmSignItem>> SIGN_ITEMS = new ArrayList<>();
    private static final List<Supplier<CharmHangingSignItem>> HANGING_SIGN_ITEMS = new ArrayList<>();
    static final Map<String, Map<String, Supplier<? extends Item>>> CREATIVE_TAB_ITEMS = new HashMap<>();
    static final List<Supplier<WoodType>> WOOD_TYPES = new ArrayList<>();
    static final List<String> WOOD_NAMES = new ArrayList<>();
    static final List<String> BOAT_IDS = new ArrayList<>();

    @Override
    public void runWhenEnabled() {
        // Boat planks can't be set early so resolve them here.
        for (var entry : BOAT_PLANKS_IDENTIFIERS.entrySet()) {
            var planks = BuiltInRegistries.BLOCK.getOptional(entry.getValue());
            planks.ifPresent(block -> entry.getKey().planks = block);
        }

        // Sign and hanging sign blocks can't be set early so resolve them here.
        for (var supplier : SIGN_ITEMS) {
            var sign = supplier.get();
            ((StandingAndWallBlockItemAccessor)sign).setWallBlock(sign.getWallSignBlock().get());
            ((BlockItemAccessor)sign).setBlock(sign.getSignBlock().get());
        }
        for (var supplier : HANGING_SIGN_ITEMS) {
            var sign = supplier.get();
            ((StandingAndWallBlockItemAccessor)sign).setWallBlock(sign.getWallSignBlock().get());
            ((BlockItemAccessor)sign).setBlock(sign.getSignBlock().get());
        }
    }

    public static Pair<Supplier<CharmBoatItem>, Supplier<CharmBoatItem>> registerBoat(ICommonRegistry registry, CharmFeature feature, IVariantWoodMaterial material) {
        var woodName = material.getSerializedName();
        var boatType = Boat.Type.valueOf((feature.getModId() + "_" + woodName).toUpperCase(Locale.ROOT));
        var boat = registry.item(woodName + "_boat", () -> new CharmBoatItem(feature, false, boatType));
        var chestBoat = registry.item(woodName + "_chest_boat", () -> new CharmBoatItem(feature, true, boatType));

        TYPE_TO_BOAT.put(boatType, boat);
        TYPE_TO_CHEST_BOAT.put(boatType, chestBoat);
        BOAT_PLANKS_IDENTIFIERS.put(boatType, new ResourceLocation(feature.getModId(), material.getSerializedName() + "_planks"));

        VariantChestBoats.registerChestBoat(boat, chestBoat);
        VariantChestBoats.registerChestLayerColor(material);

        addCreativeTabItem(feature.getModId(), "boat", boat);
        addCreativeTabItem(feature.getModId(), "chest_boat", chestBoat);

        return Pair.of(boat, chestBoat);
    }

    public static Pair<Supplier<CharmWoodButtonBlock>, Supplier<CharmWoodButtonBlock.BlockItem>> registerButton(ICommonRegistry registry, CharmFeature feature, IVariantWoodMaterial material) {
        var id = material.getSerializedName() + "_button";
        var button = registry.block(id, () -> new CharmWoodButtonBlock(feature, material));
        var buttonItem = registry.item(id, () -> new CharmWoodButtonBlock.BlockItem(feature, button));

        addCreativeTabItem(feature.getModId(), "button", buttonItem);

        return Pair.of(button, buttonItem);
    }

    public static Pair<Supplier<CharmDoorBlock>, Supplier<CharmDoorBlock.BlockItem>> registerDoor(ICommonRegistry registry, CharmFeature feature, IVariantWoodMaterial material) {
        var id = material.getSerializedName() + "_door";
        var door = registry.block(id, () -> new CharmDoorBlock(feature, material));
        var doorItem = registry.item(id, () -> new CharmDoorBlock.BlockItem(feature, door));

        addCreativeTabItem(feature.getModId(), "door", doorItem);

        return Pair.of(door, doorItem);
    }

    public static Pair<Supplier<CharmFenceBlock>, Supplier<CharmFenceBlock.BlockItem>> registerFence(ICommonRegistry registry, CharmFeature feature, IVariantWoodMaterial material) {
        var id = material.getSerializedName() + "_fence";
        var fence = registry.block(id, () -> new CharmFenceBlock(feature, material));
        var fenceItem = registry.item(id, () -> new CharmFenceBlock.BlockItem(feature, fence));

        registry.ignite(fence); // Fences can set on fire.
        addCreativeTabItem(feature.getModId(), "fence", fenceItem);

        return Pair.of(fence, fenceItem);
    }

    public static Pair<Supplier<CharmFenceGateBlock>, Supplier<CharmFenceGateBlock.BlockItem>> registerGate(ICommonRegistry registry, CharmFeature feature, IVariantWoodMaterial material) {
        var id = material.getSerializedName() + "_fence_gate";
        var gate = registry.block(id, () -> new CharmFenceGateBlock(feature, material));
        var gateItem = registry.item(id, () -> new CharmFenceGateBlock.BlockItem(feature, gate));

        registry.ignite(gate); // Gates can set on fire.
        addCreativeTabItem(feature.getModId(), "fence_gate", gateItem);

        return Pair.of(gate, gateItem);
    }

    public static Pair<Supplier<CharmLeavesBlock>, Supplier<CharmLeavesBlock.BlockItem>> registerLeaves(ICommonRegistry registry, CharmFeature feature, IVariantWoodMaterial material) {
        var id = material.getSerializedName() + "_leaves";
        var leaves = registry.block(id, () -> new CharmLeavesBlock(feature, material));
        var leavesItem = registry.item(id, () -> new CharmLeavesBlock.BlockItem(feature, leaves));

        registry.ignite(leaves); // Leaves can set on fire.
        addCreativeTabItem(feature.getModId(), "leaves", leavesItem);

        return Pair.of(leaves, leavesItem);
    }

    public static Map<String, Pair<Supplier<CharmLogBlock>, Supplier<CharmLogBlock.BlockItem>>> registerLog(ICommonRegistry registry, CharmFeature feature, IVariantWoodMaterial material) {
        var logId = material.getSerializedName() + "_log";
        var woodId = material.getSerializedName() + "_wood";
        var strippedLogId = "stripped_" + material.getSerializedName() + "_log";
        var strippedWoodId = "stripped_" + material.getSerializedName() + "_wood";

        var log = registry.block(logId, () -> new CharmLogBlock(feature, material));
        var logItem = registry.item(logId, () -> new CharmLogBlock.BlockItem(feature, log));
        var strippedLog = registry.block(strippedLogId, () -> new CharmLogBlock(feature, material));
        var strippedLogItem = registry.item(strippedLogId, () -> new CharmLogBlock.BlockItem(feature, strippedLog));

        var wood = registry.block(woodId, () -> new CharmLogBlock(feature, material));
        var woodItem = registry.item(woodId, () -> new CharmLogBlock.BlockItem(feature, wood));
        var strippedWood = registry.block(strippedWoodId, () -> new CharmLogBlock(feature, material));
        var strippedWoodItem = registry.item(strippedWoodId, () -> new CharmLogBlock.BlockItem(feature, strippedWood));

        // Logs and wood can set on fire.
        registry.ignite(log);
        registry.ignite(wood);
        registry.ignite(strippedLog);
        registry.ignite(strippedWood);

        // Logs and wood can be stripped.
        registry.strippable(log, strippedLog);
        registry.strippable(wood, strippedWood);

        Map<String, Pair<Supplier<CharmLogBlock>, Supplier<CharmLogBlock.BlockItem>>> map = new HashMap<>();

        map.put(logId, Pair.of(log, logItem));
        map.put(woodId, Pair.of(wood, woodItem));
        map.put(strippedLogId, Pair.of(strippedLog, strippedLogItem));
        map.put(strippedWoodId, Pair.of(strippedWood, strippedWoodItem));

        addCreativeTabItem(feature.getModId(), "log", logItem);
        addCreativeTabItem(feature.getModId(), "wood", woodItem);
        addCreativeTabItem(feature.getModId(), "stripped_log", strippedLogItem);
        addCreativeTabItem(feature.getModId(), "stripped_wood", strippedWoodItem);

        return map;
    }

    public static Pair<Supplier<CharmPressurePlateBlock>, Supplier<CharmPressurePlateBlock.BlockItem>> registerPressurePlate(ICommonRegistry registry, CharmFeature feature, IVariantWoodMaterial material) {
        var id = material.getSerializedName() + "_pressure_plate";
        var pressurePlate = registry.block(id, () -> new CharmPressurePlateBlock(feature, material));
        var pressurePlateItem = registry.item(id, () -> new CharmPressurePlateBlock.BlockItem(feature, pressurePlate));

        addCreativeTabItem(feature.getModId(), "pressure_plate", pressurePlateItem);

        return Pair.of(pressurePlate, pressurePlateItem);
    }

    public static Map<String, Pair<Supplier<? extends Block>, Supplier<? extends BlockItem>>> registerPlanksSlabsAndStairs(ICommonRegistry registry, CharmFeature feature, IVariantWoodMaterial material) {
        var planksId = material.getSerializedName() + "_planks";
        var slabId = material.getSerializedName() + "_slab";
        var stairsId = material.getSerializedName() + "_stairs";

        var planks = registry.block(planksId, () -> new CharmPlanksBlock(feature, material));
        var planksItem = registry.item(planksId, () -> new CharmPlanksBlock.BlockItem(feature, planks));

        var slab = registry.block(slabId, () -> new CharmSlabBlock(feature, material));
        var slabItem = registry.item(slabId, () -> new CharmSlabBlock.BlockItem(feature, slab));

        var stairs = registry.stairsBlock(stairsId, feature, material, () -> planks.get().defaultBlockState());

        registry.ignite(planks); // Planks can set on fire.
        registry.ignite(slab); // Slabs can set on fire.
        registry.ignite(stairs.getFirst()); // Stairs can set on fire.

        var stairsItem = stairs.getSecond();

        Map<String, Pair<Supplier<? extends Block >, Supplier<? extends BlockItem>>> map = new HashMap<>();

        map.put(planksId, Pair.of(planks, planksItem));
        map.put(slabId, Pair.of(slab, slabItem));
        map.put(stairsId, Pair.of(stairs.getFirst(), stairsItem));

        addCreativeTabItem(feature.getModId(), "planks", planksItem);
        addCreativeTabItem(feature.getModId(), "slab", slabItem);
        addCreativeTabItem(feature.getModId(), "stairs", stairsItem);

        return map;
    }

    public static Pair<Supplier<CharmSaplingBlock>, Supplier<CharmSaplingBlock.BlockItem>> registerSapling(ICommonRegistry registry, CharmFeature feature, IVariantWoodMaterial material) {
        var saplingId = material.getSerializedName() + "_sapling";
        var treeId = material.getSerializedName() + "_tree";
        var key = ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(feature.getModId(), treeId));

        var sapling = registry.block(saplingId, () -> new CharmSaplingBlock(feature, material, new AbstractTreeGrower() {
            @Override
            protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean hasBees) {
            return key;
            }
        }));
        var saplingItem = registry.item(saplingId, () -> new CharmSaplingBlock.BlockItem(feature, sapling));

        addCreativeTabItem(feature.getModId(), "sapling", saplingItem);

        return Pair.of(sapling, saplingItem);
    }

    public static Map<String, Pair<Supplier<? extends SignBlock>, Supplier<? extends BlockItem>>> registerSign(ICommonRegistry registry, CharmFeature feature, IVariantWoodMaterial material) {
        var signId = material.getSerializedName() + "_sign";
        var wallSignId = material.getSerializedName() + "_wall_sign";
        var woodType = getWoodType(feature, material);

        var sign = registry.block(signId, () -> new CharmStandingSignBlock(feature, material, woodType));
        var wallSign = registry.wallSignBlock(wallSignId, feature, material, sign, woodType);
        var signItem = registry.item(signId, () -> new CharmSignItem(feature, material, sign, wallSign));

        SIGN_ITEMS.add(signItem);

        Map<String, Pair<Supplier<? extends SignBlock>, Supplier<? extends BlockItem>>> map = new HashMap<>();
        map.put(signId, Pair.of(sign, signItem));
        map.put(wallSignId, Pair.of(wallSign, signItem));

        // Associate with the sign block entity.
        registry.blockEntityBlocks(() -> BlockEntityType.SIGN, List.of(sign, wallSign));

        addCreativeTabItem(feature.getModId(), "sign", signItem);

        return map;
    }

    public static Map<String, Pair<Supplier<? extends SignBlock>, Supplier<? extends BlockItem>>> registerHangingSign(ICommonRegistry registry, CharmFeature feature, IVariantWoodMaterial material) {
        var signId = material.getSerializedName() + "_hanging_sign";
        var wallSignId = material.getSerializedName() + "_wall_hanging_sign";
        var woodType = getWoodType(feature, material);

        var sign = registry.block(signId, () -> new CharmCeilingHangingSignBlock(feature, material, woodType));
        var wallSign = registry.wallHangingSignBlock(wallSignId, feature, material, sign, woodType);
        var signItem = registry.item(signId, () -> new CharmHangingSignItem(feature, material, sign, wallSign));

        HANGING_SIGN_ITEMS.add(signItem);

        Map<String, Pair<Supplier<? extends SignBlock>, Supplier<? extends BlockItem>>> map = new HashMap<>();
        map.put(signId, Pair.of(sign, signItem));
        map.put(wallSignId, Pair.of(wallSign, signItem));

        // Associate with the hanging sign block entity.
        registry.blockEntityBlocks(() -> BlockEntityType.HANGING_SIGN, List.of(sign, wallSign));

        addCreativeTabItem(feature.getModId(), "hanging_sign", signItem);

        return map;
    }

    public static Pair<Supplier<CharmTrapdoorBlock>, Supplier<CharmTrapdoorBlock.BlockItem>> registerTrapdoor(ICommonRegistry registry, CharmFeature feature, IVariantWoodMaterial material) {
        var id = material.getSerializedName() + "_trapdoor";
        var trapdoor = registry.block(id, () -> new CharmTrapdoorBlock(feature, material));
        var trapdoorItem = registry.item(id, () -> new CharmTrapdoorBlock.BlockItem(feature, trapdoor));

        addCreativeTabItem(feature.getModId(), "trapdoor", trapdoorItem);

        return Pair.of(trapdoor, trapdoorItem);
    }

    public static void registerBarrel(ICommonRegistry registry, IVariantWoodMaterial material) {
        VariantBarrels.registerBarrel(registry, material);
    }

    public static void registerBookshelf(ICommonRegistry registry, IVariantWoodMaterial material) {
        VariantBookshelves.registerBookshelf(registry, material);
    }

    public static void registerChiseledBookshelf(ICommonRegistry registry, IVariantWoodMaterial material) {
        VariantChiseledBookshelves.registerChiseledBookshelf(registry, material);
    }

    public static void registerChest(ICommonRegistry registry, IVariantWoodMaterial material) {
        VariantChests.registerChest(registry, material);
    }

    public static void registerTrappedChest(ICommonRegistry registry, IVariantWoodMaterial material) {
        VariantChests.registerTrappedChest(registry, material);
    }

    public static void registerLadder(ICommonRegistry registry, IVariantWoodMaterial material) {
        VariantLadders.registerLadder(registry, material);
    }

    public static Supplier<WoodType> registerWoodType(ICommonRegistry registry, IVariantWoodMaterial material) {
        var name = material.getSerializedName();
        var woodType = registry.woodType(name, material);
        WOOD_TYPES.add(woodType);
        WOOD_NAMES.add(name);
        return woodType;
    }

    @Nullable
    public static BoatItem getBoatByType(Boat.Type boatType) {
        return TYPE_TO_BOAT.getOrDefault(boatType, () -> null).get();
    }

    @Nullable
    public static BoatItem getChestBoatByType(Boat.Type boatType) {
        return TYPE_TO_CHEST_BOAT.getOrDefault(boatType, () -> null).get();
    }

    public static WoodType getWoodType(CharmFeature feature, IVariantWoodMaterial material) {
        return getWoodTypeByName(feature.getModId() + "_" + material.getSerializedName());
    }

    public static WoodType getWoodTypeByName(String name) {
        return WoodType.values().filter(w -> w.name().equals(name)).findFirst().orElseThrow();
    }

    private static void addCreativeTabItem(String modId, String name, Supplier<? extends Item> item) {
        CREATIVE_TAB_ITEMS.computeIfAbsent(modId, m -> new LinkedHashMap<>()).put(name, item);
    }
}
