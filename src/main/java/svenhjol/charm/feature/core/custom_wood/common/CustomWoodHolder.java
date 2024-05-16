package svenhjol.charm.feature.core.custom_wood.common;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import svenhjol.charm.api.iface.IVariantWoodMaterial;
import svenhjol.charm.feature.core.custom_wood.CustomWood;
import svenhjol.charm.feature.core.custom_wood.types.*;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonRegistry;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Holds all registered wood blocks, items etc.
 */
@SuppressWarnings("unused")
public class CustomWoodHolder {
    private final CustomWood feature;
    private final CommonFeature owner;
    private final IVariantWoodMaterial material;

    private CustomBarrel barrel;
    private CustomBoat boat;
    private CustomBookshelf bookshelf;
    private CustomButton button;
    private CustomChest chest;
    private CustomChiseledBookshelf chiseledBookshelf;
    private CustomDoor door;
    private CustomFence fence;
    private CustomGate gate;
    private CustomHangingSign hangingSign;
    private CustomLadder ladder;
    private CustomLeaves leaves;
    private CustomLogBlock log;
    private CustomPlanks planks;
    private CustomPressurePlate pressurePlate;
    private CustomSapling sapling;
    private CustomSign sign;
    private CustomSlab slab;
    private CustomStairs stairs;
    private CustomTrapdoor trapdoor;
    private CustomTrappedChest trappedChest;
    private CustomWoodBlock wood;

    public CustomWoodHolder(CustomWood feature, CommonFeature owner, CustomWoodDefinition definition) {
        this.feature = feature;
        this.owner = owner;
        this.material = definition.material();

        definition.types().forEach(type -> {
            switch (type) {
                case BARREL -> barrel = new CustomBarrel(this);
                case BOAT -> boat = new CustomBoat(this);
                case BOOKSHELF -> bookshelf = new CustomBookshelf(this);
                case BUTTON -> button = new CustomButton(this);
                case CHEST -> chest = new CustomChest(this);
                case CHISELED_BOOKSHELF -> chiseledBookshelf = new CustomChiseledBookshelf(this);
                case DOOR -> door = new CustomDoor(this);
                case FENCE -> fence = new CustomFence(this);
                case GATE -> gate = new CustomGate(this);
                case HANGING_SIGN -> hangingSign = new CustomHangingSign(this);
                case LADDER -> ladder = new CustomLadder(this);
                case LEAVES -> leaves = new CustomLeaves(this);
                case LOG -> log = new CustomLogBlock(this);
                case PLANKS -> planks = new CustomPlanks(this);
                case PRESSURE_PLATE -> pressurePlate = new CustomPressurePlate(this);
                case SAPLING -> sapling = new CustomSapling(this);
                case SIGN -> sign = new CustomSign(this);
                case SLAB -> slab = new CustomSlab(this);
                case STAIRS -> planks().ifPresent(planks -> stairs = new CustomStairs(this, planks));
                case TRAPDOOR -> trapdoor = new CustomTrapdoor(this);
                case TRAPPED_CHEST -> trappedChest = new CustomTrappedChest(this);
                case WOOD -> wood = new CustomWoodBlock(this);
            }
        });
    }

    public void addCreativeTabItem(CustomType customType, Supplier<? extends Item> item) {
        feature().handlers.addCreativeTabItem(ownerId(), customType, item);
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

    public IVariantWoodMaterial getMaterial() {
        return material;
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

    public Optional<CustomBarrel> barrel() {
        return Optional.ofNullable(barrel);
    }

    public Optional<CustomBoat> boat() {
        return Optional.ofNullable(boat);
    }

    public Optional<CustomBookshelf> bookshelf() {
        return Optional.ofNullable(bookshelf);
    }

    public Optional<CustomButton> button() {
        return Optional.ofNullable(button);
    }

    public Optional<CustomChest> chest() {
        return Optional.ofNullable(chest);
    }

    public Optional<CustomChiseledBookshelf> chiseledBookshelf() {
        return Optional.ofNullable(chiseledBookshelf);
    }

    public Optional<CustomDoor> door() {
        return Optional.ofNullable(door);
    }

    public Optional<CustomFence> fence() {
        return Optional.ofNullable(fence);
    }

    public Optional<CustomGate> gate() {
        return Optional.ofNullable(gate);
    }

    public Optional<CustomHangingSign> hangingSign() {
        return Optional.ofNullable(hangingSign);
    }

    public Optional<CustomLadder> ladder() {
        return Optional.ofNullable(ladder);
    }

    public Optional<CustomLeaves> leaves() {
        return Optional.ofNullable(leaves);
    }

    public Optional<CustomLogBlock> log() {
        return Optional.ofNullable(log);
    }

    public Optional<CustomPlanks> planks() {
        return Optional.ofNullable(planks);
    }

    public Optional<CustomPressurePlate> pressurePlate() {
        return Optional.ofNullable(pressurePlate);
    }

    public Optional<CustomSapling> sapling() {
        return Optional.ofNullable(sapling);
    }

    public Optional<CustomSign> sign() {
        return Optional.ofNullable(sign);
    }

    public Optional<CustomSlab> slab() {
        return Optional.ofNullable(slab);
    }

    public Optional<CustomStairs> stairs() {
        return Optional.ofNullable(stairs);
    }

    public Optional<CustomTrapdoor> trapdoor() {
        return Optional.ofNullable(trapdoor);
    }

    public Optional<CustomTrappedChest> trappedChest() {
        return Optional.ofNullable(trappedChest);
    }

    public Optional<CustomWoodBlock> wood() {
        return Optional.ofNullable(wood);
    }
}
