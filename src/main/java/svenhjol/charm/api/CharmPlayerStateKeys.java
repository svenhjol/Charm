package svenhjol.charm.api;

import net.minecraft.util.StringRepresentable;

/**
 * @version 1.0.0
 */
public enum CharmPlayerStateKeys implements StringRepresentable {
    InsideMineshaft("mineshaft"),
    InsideStronghold("stronghold"),
    InsideNetherFortress("nether_fortress"),
    InsideShipwreck("shipwreck"),
    InsideVillage("village");

    private final String name;

    CharmPlayerStateKeys(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
