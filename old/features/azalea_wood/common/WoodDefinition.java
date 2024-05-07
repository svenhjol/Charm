package svenhjol.charm.feature.azalea_wood.common;

import svenhjol.charm.api.iface.IVariantWoodMaterial;
import svenhjol.charm.feature.custom_wood.CustomWoodDefinition;

public class WoodDefinition implements CustomWoodDefinition {
    @Override
    public IVariantWoodMaterial getMaterial() {
        return Material.AZALEA;
    }

    @Override
    public boolean boat() { return true; }

    @Override
    public boolean button() { return true; }

    @Override
    public boolean door() { return true; }

    @Override
    public boolean fence() { return true; }

    @Override
    public boolean gate() { return true; }

    @Override
    public boolean hangingSign() { return true; }

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
    public boolean wood() { return true; }
}
