package svenhjol.charm.feature.custom_wood;

import svenhjol.charm.api.iface.IVariantWoodMaterial;

/**
 * Define all the wood items implemented by a custom wood type.
 * Used by for example Charm AzaleaWood.
 * TODO: definitions can return more than just booleans.
 */
@SuppressWarnings("unused")
public interface CustomWoodDefinition {
    IVariantWoodMaterial getMaterial();

    default boolean boat() { return false; }

    default boolean button() { return false; }

    default boolean door() { return false; }

    default boolean fence() { return false; }

    default boolean gate() { return false; }

    default boolean hangingSign() { return false; }

    default boolean leaves() { return false; }

    default boolean log() { return false; }

    default boolean planks() { return false; }

    default boolean pressurePlate() { return false; }

    default boolean sapling() { return false; }

    default boolean sign() { return false; }

    default boolean slab() { return false; }

    default boolean stairs() { return false; }

    default boolean trapdoor() { return false; }

    default boolean wood() { return false; }
}
