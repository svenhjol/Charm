package svenhjol.charm.foundation.enums;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import svenhjol.charm.api.iface.IVariantWoodMaterial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public enum VanillaWood implements IVariantWoodMaterial {
    ACACIA(() -> BlockSetType.ACACIA, () -> WoodType.ACACIA, true),
    BAMBOO(() -> BlockSetType.BAMBOO, () -> WoodType.BAMBOO, true),
    BIRCH(() -> BlockSetType.BIRCH, () -> WoodType.BIRCH, true),
    CHERRY(() -> BlockSetType.CHERRY, () -> WoodType.CHERRY, true),
    CRIMSON(() -> BlockSetType.CRIMSON, () -> WoodType.CRIMSON, false),
    DARK_OAK(() -> BlockSetType.DARK_OAK, () -> WoodType.DARK_OAK, true),
    JUNGLE(() -> BlockSetType.JUNGLE, () -> WoodType.JUNGLE, true),
    MANGROVE(() -> BlockSetType.MANGROVE, () -> WoodType.MANGROVE, true),
    OAK(() -> BlockSetType.OAK, () -> WoodType.OAK, true),
    SPRUCE(() -> BlockSetType.SPRUCE, () -> WoodType.SPRUCE, true),
    WARPED(() -> BlockSetType.WARPED, () -> WoodType.WARPED, false);

    private final boolean flammable;
    private final Supplier<BlockSetType> blockSetType;
    private final Supplier<WoodType> woodType;

    VanillaWood(Supplier<BlockSetType> blockSetType, Supplier<WoodType> woodType, boolean flammable) {
        this.blockSetType = blockSetType;
        this.woodType = woodType;
        this.flammable = flammable;
    }

    @Override
    public boolean isFlammable() {
        return flammable;
    }

    @Override
    public SoundType soundType() {
        return SoundType.WOOD;
    }

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ENGLISH);
    }

    @Override
    public BlockSetType blockSetType() {
        return blockSetType.get();
    }

    @Override
    public WoodType woodType() {
        return woodType.get();
    }

    public static List<IVariantWoodMaterial> getTypes() {
        return Arrays.stream(values())
            .collect(Collectors.toList());
    }

    public static List<IVariantWoodMaterial> getTypesWithout(IVariantWoodMaterial... types) {
        var typesList = new ArrayList<>(Arrays.asList(types));
        return Arrays.stream(values()).filter(t -> !typesList.contains(t))
            .collect(Collectors.toList());
    }
}
