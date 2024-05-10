package svenhjol.charm.feature.custom_wood.common;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import svenhjol.charm.api.iface.IVariantWoodMaterial;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonRegistry;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Holds all registered wood blocks, items etc.
 */
@SuppressWarnings("unused")
public class CustomWoodHolder {
    private final CommonFeature owner;
    private final IVariantWoodMaterial material;
    private CustomBoat boat;
    private CustomButton button;
    private CustomDoor door;
    private CustomFence fence;
    private CustomGate gate;
    private CustomHangingSign hangingSign;
    private CustomLeaves leaves;
    private CustomLogBlock log;
    private CustomPlanks planks;
    private CustomPressurePlate pressurePlate;
    private CustomSapling sapling;
    private CustomSign sign;
    private CustomSlab slab;
    private CustomStairs stairs;
    private CustomTrapdoor trapdoor;
    private CustomWoodBlock wood;

    public CustomWoodHolder(CommonFeature owner, CustomWoodDefinition definition) {
        this.owner = owner;
        this.material = definition.getMaterial();

        if (definition.boat()) {
            boat = new CustomBoat(this);
        }

        if (definition.button()) {
            button = new CustomButton(this);
        }

        if (definition.door()) {
            door = new CustomDoor(this);
        }

        if (definition.fence()) {
            fence = new CustomFence(this);
        }

        if (definition.gate()) {
            gate = new CustomGate(this);
        }

        if (definition.hangingSign()) {
            hangingSign = new CustomHangingSign(this);
        }

        if (definition.leaves()) {
            leaves = new CustomLeaves(this);
        }

        if (definition.log()) {
            log = new CustomLogBlock(this);
        }

        if (definition.planks()) {
            planks = new CustomPlanks(this);
        }

        if (definition.pressurePlate()) {
            pressurePlate = new CustomPressurePlate(this);
        }

        if (definition.sapling()) {
            sapling = new CustomSapling(this, 0f);
        }

        if (definition.sign()) {
            sign = new CustomSign(this);
        }

        if (definition.slab()) {
            slab = new CustomSlab(this);
        }

        if (definition.stairs()) {
            planks().ifPresent(planks -> stairs = new CustomStairs(this, planks));
        }

        if (definition.trapdoor()) {
            trapdoor = new CustomTrapdoor(this);
        }

        if (definition.wood()) {
            wood = new CustomWoodBlock(this);
        }
    }

    public void addCreativeTabItem(String name, Supplier<? extends Item> item) {
        CustomWoodHelper.addCreativeTabItem(ownerId(), name, item);
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

    public Optional<CustomBoat> boat() {
        return Optional.ofNullable(boat);
    }

    public Optional<CustomButton> button() {
        return Optional.ofNullable(button);
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

    public Optional<CustomWoodBlock> wood() {
        return Optional.ofNullable(wood);
    }
}
