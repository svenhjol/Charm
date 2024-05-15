package svenhjol.charm.feature.extra_wood.vanilla_wood_variants.common;

import svenhjol.charm.api.iface.IVariantWoodMaterial;
import svenhjol.charm.feature.custom_wood.common.CustomWoodDefinition;

public record WoodDefinition(IVariantWoodMaterial material) implements CustomWoodDefinition {
    @Override
    public boolean barrel() {
        return true;
    }

    @Override
    public boolean bookshelf() {
        return true;
    }

    @Override
    public boolean chest() {
        return true;
    }

    @Override
    public boolean chiseledBookshelf() {
        return true;
    }

    @Override
    public boolean ladder() {
        return true;
    }

    @Override
    public boolean trappedChest() {
        return true;
    }
}
