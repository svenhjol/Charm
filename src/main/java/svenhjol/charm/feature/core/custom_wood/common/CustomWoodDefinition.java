package svenhjol.charm.feature.core.custom_wood.common;

import svenhjol.charm.api.iface.CustomWoodMaterial;

import java.util.List;

/**
 * Define all the wood items implemented by a custom wood type.
 * Used by for example Charm AzaleaWood.
 */
@SuppressWarnings("unused")
public interface CustomWoodDefinition {
    CustomWoodMaterial material();

    List<CustomType> types();
}
