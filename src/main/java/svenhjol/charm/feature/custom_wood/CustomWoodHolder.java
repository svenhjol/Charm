package svenhjol.charm.feature.custom_wood;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import svenhjol.charm.api.iface.IVariantWoodMaterial;
import svenhjol.charm.feature.custom_wood.registry.*;
import svenhjol.charm.foundation.common.CommonRegistry;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Holds all registered wood blocks, items etc.
 */
@SuppressWarnings("unused")
public class CustomWoodHolder {
    private final CommonRegistry registry;
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

    public CustomWoodHolder(CommonRegistry registry, CustomWoodDefinition definition) {
        this.registry = registry;
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
            getPlanks().ifPresent(planks -> stairs = new CustomStairs(this, planks));
        }

        if (definition.trapdoor()) {
            trapdoor = new CustomTrapdoor(this);
        }

        if (definition.wood()) {
            wood = new CustomWoodBlock(this);
        }
    }

    public void addCreativeTabItem(String name, Supplier<? extends Item> item) {
        CustomWoodHelper.addCreativeTabItem(getModId(), name, item);
    }

    public CommonRegistry getRegistry() {
        return registry;
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

    public String getModId() {
        return registry.id();
    }

    public Optional<CustomBoat> getBoat() {
        return Optional.ofNullable(boat);
    }

    public Optional<CustomButton> getButton() {
        return Optional.ofNullable(button);
    }

    public Optional<CustomDoor> getDoor() {
        return Optional.ofNullable(door);
    }

    public Optional<CustomFence> getFence() {
        return Optional.ofNullable(fence);
    }

    public Optional<CustomGate> getGate() {
        return Optional.ofNullable(gate);
    }

    public Optional<CustomHangingSign> getHangingSign() {
        return Optional.ofNullable(hangingSign);
    }

    public Optional<CustomLeaves> getLeaves() {
        return Optional.ofNullable(leaves);
    }

    public Optional<CustomLogBlock> getLog() {
        return Optional.ofNullable(log);
    }

    public Optional<CustomPlanks> getPlanks() {
        return Optional.ofNullable(planks);
    }

    public Optional<CustomPressurePlate> getPressurePlate() {
        return Optional.ofNullable(pressurePlate);
    }

    public Optional<CustomSapling> getSapling() {
        return Optional.ofNullable(sapling);
    }

    public Optional<CustomSign> getSign() {
        return Optional.ofNullable(sign);
    }

    public Optional<CustomSlab> getSlab() {
        return Optional.ofNullable(slab);
    }

    public Optional<CustomStairs> getStairs() {
        return Optional.ofNullable(stairs);
    }

    public Optional<CustomTrapdoor> getTrapdoor() {
        return Optional.ofNullable(trapdoor);
    }

    public Optional<CustomWoodBlock> getWood() {
        return Optional.ofNullable(wood);
    }
}
