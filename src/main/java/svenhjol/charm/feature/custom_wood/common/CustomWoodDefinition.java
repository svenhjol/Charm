package svenhjol.charm.feature.custom_wood.common;

import svenhjol.charm.api.iface.IVariantWoodMaterial;

import java.util.List;

/**
 * Define all the wood items implemented by a custom wood type.
 * Used by for example Charm AzaleaWood.
 */
@SuppressWarnings("unused")
public interface CustomWoodDefinition {
    IVariantWoodMaterial material();

    List<CustomType> types();
}
