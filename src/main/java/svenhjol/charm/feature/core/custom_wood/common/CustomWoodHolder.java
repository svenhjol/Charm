package svenhjol.charm.feature.core.custom_wood.common;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonRegistry;
import svenhjol.charm.charmony.iface.CustomWoodMaterial;
import svenhjol.charm.feature.core.custom_wood.CustomWood;
import svenhjol.charm.feature.core.custom_wood.holders.*;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Holds all registered wood blocks, items etc.
 */
@SuppressWarnings("unused")
public class CustomWoodHolder {
    private final CustomWood feature;
    private final CommonFeature owner;
    private final CustomWoodMaterial material;
    private final CustomWoodDefinition definition;

    private BarrelHolder barrel;
    private BoatHolder boat;
    private BookshelfHolder bookshelf;
    private ButtonHolder button;
    private ChestHolder chest;
    private ChiseledBookshelfHolder chiseledBookshelf;
    private DoorHolder door;
    private FenceHolder fence;
    private GateHolder gate;
    private HangingSignHolder hangingSign;
    private LadderHolder ladder;
    private LeavesHolder leaves;
    private LogBlockHolder log;
    private PlanksHolder planks;
    private PressurePlateHolder pressurePlate;
    private Sapling sapling;
    private SignHolder sign;
    private SlabHolder slab;
    private StairsHolder stairs;
    private TrapdoorHolder trapdoor;
    private TrappedChestHolder trappedChest;
    private WoodBlockHolder wood;

    public CustomWoodHolder(CustomWood feature, CommonFeature owner, CustomWoodDefinition definition) {
        this.feature = feature;
        this.owner = owner;
        this.definition = definition;
        this.material = definition.material();

        definition.types().forEach(type -> {
            switch (type) {
                case BARREL -> barrel = new BarrelHolder(this);
                case BOAT -> boat = new BoatHolder(this);
                case BOOKSHELF -> bookshelf = new BookshelfHolder(this);
                case BUTTON -> button = new ButtonHolder(this);
                case CHEST -> chest = new ChestHolder(this);
                case CHISELED_BOOKSHELF -> chiseledBookshelf = new ChiseledBookshelfHolder(this);
                case DOOR -> door = new DoorHolder(this);
                case FENCE -> fence = new FenceHolder(this);
                case GATE -> gate = new GateHolder(this);
                case HANGING_SIGN -> hangingSign = new HangingSignHolder(this);
                case LADDER -> ladder = new LadderHolder(this);
                case LEAVES -> leaves = new LeavesHolder(this);
                case LOG -> log = new LogBlockHolder(this);
                case PLANKS -> planks = new PlanksHolder(this);
                case PRESSURE_PLATE -> pressurePlate = new PressurePlateHolder(this);
                case SAPLING -> sapling = new Sapling(this);
                case SIGN -> sign = new SignHolder(this);
                case SLAB -> slab = new SlabHolder(this);
                case STAIRS -> planks().ifPresent(planks -> stairs = new StairsHolder(this, planks));
                case TRAPDOOR -> trapdoor = new TrapdoorHolder(this);
                case TRAPPED_CHEST -> trappedChest = new TrappedChestHolder(this);
                case WOOD -> wood = new WoodBlockHolder(this);
            }
        });
    }

    public void addItemToCreativeTab(Supplier<? extends Item> item, CustomType customType) {
        var map = definition.creativeMenuPosition();
        if (map.isEmpty() || map.get(customType) == null) {
            throw new RuntimeException("Could not find creative tab position for custom type: " + customType);
        }

        var after = map.get(customType);
        feature().registers.addItemToCreativeTab(item, after, customType);
    }

    public CustomWood feature() {
        return feature;
    }

    public CommonFeature owner() {
        return owner;
    }

    public CommonRegistry ownerRegistry() {
        return owner().registry();
    }

    public CustomWoodMaterial getMaterial() {
        return material;
    }

    public CustomWoodDefinition getDefinition() {
        return definition;
    }

    public String getMaterialName() {
        return material.getSerializedName();
    }

    public WoodType woodType() {
        return material.woodType();
    }

    public BlockSetType blockSetType() {
        return material.blockSetType();
    }

    public String ownerId() {
        return ownerRegistry().id();
    }

    public Optional<BarrelHolder> barrel() {
        return Optional.ofNullable(barrel);
    }

    public Optional<BoatHolder> boat() {
        return Optional.ofNullable(boat);
    }

    public Optional<BookshelfHolder> bookshelf() {
        return Optional.ofNullable(bookshelf);
    }

    public Optional<ButtonHolder> button() {
        return Optional.ofNullable(button);
    }

    public Optional<ChestHolder> chest() {
        return Optional.ofNullable(chest);
    }

    public Optional<ChiseledBookshelfHolder> chiseledBookshelf() {
        return Optional.ofNullable(chiseledBookshelf);
    }

    public Optional<DoorHolder> door() {
        return Optional.ofNullable(door);
    }

    public Optional<FenceHolder> fence() {
        return Optional.ofNullable(fence);
    }

    public Optional<GateHolder> gate() {
        return Optional.ofNullable(gate);
    }

    public Optional<HangingSignHolder> hangingSign() {
        return Optional.ofNullable(hangingSign);
    }

    public Optional<LadderHolder> ladder() {
        return Optional.ofNullable(ladder);
    }

    public Optional<LeavesHolder> leaves() {
        return Optional.ofNullable(leaves);
    }

    public Optional<LogBlockHolder> log() {
        return Optional.ofNullable(log);
    }

    public Optional<PlanksHolder> planks() {
        return Optional.ofNullable(planks);
    }

    public Optional<PressurePlateHolder> pressurePlate() {
        return Optional.ofNullable(pressurePlate);
    }

    public Optional<Sapling> sapling() {
        return Optional.ofNullable(sapling);
    }

    public Optional<SignHolder> sign() {
        return Optional.ofNullable(sign);
    }

    public Optional<SlabHolder> slab() {
        return Optional.ofNullable(slab);
    }

    public Optional<StairsHolder> stairs() {
        return Optional.ofNullable(stairs);
    }

    public Optional<TrapdoorHolder> trapdoor() {
        return Optional.ofNullable(trapdoor);
    }

    public Optional<TrappedChestHolder> trappedChest() {
        return Optional.ofNullable(trappedChest);
    }

    public Optional<WoodBlockHolder> wood() {
        return Optional.ofNullable(wood);
    }
}
