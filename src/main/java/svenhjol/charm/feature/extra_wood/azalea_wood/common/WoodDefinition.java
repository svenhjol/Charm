package svenhjol.charm.feature.extra_wood.azalea_wood.common;

import svenhjol.charm.api.iface.IVariantWoodMaterial;
import svenhjol.charm.feature.custom_wood.common.CustomWoodDefinition;

public class WoodDefinition implements CustomWoodDefinition {
    @Override
    public IVariantWoodMaterial material() {
        return Material.AZALEA;
    }

    @Override
    public boolean barrel() {
        return true;
    }

    @Override
    public boolean boat() { return true; }

    @Override
    public boolean bookshelf() {
        return true;
    }

    @Override
    public boolean button() { return true; }

    @Override
    public boolean chest() {
        return true;
    }

    @Override
    public boolean chiseledBookshelf() {
        return true;
    }

    @Override
    public boolean door() { return true; }

    @Override
    public boolean fence() { return true; }

    @Override
    public boolean gate() { return true; }

    @Override
    public boolean hangingSign() { return true; }

    @Override
    public boolean ladder() {
        return true;
    }

    @Override
    public boolean log() { return true; }

    @Override
    public boolean planks() { return true; }

    @Override
    public boolean pressurePlate() { return true; }

    @Override
    public boolean sign() { return true; }

    @Override
    public boolean slab() { return true; }

    @Override
    public boolean stairs() { return true; }

    @Override
    public boolean trapdoor() { return true; }

    @Override
    public boolean trappedChest() {
        return true;
    }

    @Override
    public boolean wood() { return true; }
}
