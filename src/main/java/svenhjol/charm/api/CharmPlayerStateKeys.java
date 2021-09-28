package svenhjol.charm.api;

import net.minecraft.util.StringRepresentable;

/**
 * @version 1.1.0
 */
@SuppressWarnings("unused")
public enum CharmPlayerStateKeys implements StringRepresentable {
    InsideVillage("village"),
    InsideMineshaft("mineshaft"),
    InsideJunglePyramid("jungle_pyramid"),
    InsideDesertPyramid("desert_pyramid"),
    InsideStronghold("stronghold"),
    InsideSwampHut("swamp_hut"),
    InsideOceanRuin("ocean_ruin"),
    InsideOceanMonument("ocean_monument"),
    InsideIgloo("igloo"),
    InsidePillagerOutpost("pillager_outpost"),
    InsideMansion("mansion"),
    InsideNetherFortress("nether_fortress"),
    InsideBastionRemnant("bastion_remnant"),
    InsideEndCity("end_city"),
    InsideOverworldRuin("overworld_ruin"),
    InsideNetherRuin("nether_ruin"),
    InsideEndRuin("end_ruin");

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
