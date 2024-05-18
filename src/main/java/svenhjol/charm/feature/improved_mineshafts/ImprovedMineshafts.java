package svenhjol.charm.feature.improved_mineshafts;

import svenhjol.charm.feature.improved_mineshafts.common.Handlers;
import svenhjol.charm.feature.improved_mineshafts.common.Registers;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

import java.util.List;

@Feature(description = "Adds decoration and more ores to mineshafts.")
public class ImprovedMineshafts extends CommonFeature {
    public final Registers registers;
    public final Handlers handlers;

    @Configurable(name = "Corridor floor blocks", description = "Chance (out of 1.0) of blocks such as candles and ores spawning on the floor of corridors.")
    public static double floorBlockChance = 0.03d;

    @Configurable(name = "Corridor ceiling blocks", description = "Chance (out of 1.0) of blocks such as lanterns spawning on the ceiling of corridors.")
    public static double ceilingBlockChance = 0.02d;

    @Configurable(name = "Corridor block piles", description = "Chance (out of 1.0) of stone, gravel and ore spawning at the entrance of corridors.")
    public static double blockPileChance = 0.2d;

    @Configurable(name = "Room blocks", description = "Chance (out of 1.0) for a moss or precious ore block to spawn on a single block of the central mineshaft room.")
    public static double roomBlockChance = 0.25d;

    @Configurable(name = "Extra minecarts", description = "Chance (out of 1.0) for a minecart to spawn in a corridor. Minecart loot is chosen from the 'Minecart loot' tables.")
    public static double minecartChance = 0.2d;

    @Configurable(name = "Minecart loot", description = "List of loot tables that will be used to populate custom minecarts.")
    public static List<String> minecartLoot = List.of(
        "minecraft:chests/simple_dungeon",
        "minecraft:chests/abandoned_mineshaft",
        "minecraft:chests/village/village_temple",
        "minecraft:chests/village/village_cartographer",
        "minecraft:chests/village/village_mason",
        "minecraft:chests/village/village_toolsmith",
        "minecraft:chests/village/village_weaponsmith"
    );

    public ImprovedMineshafts(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
    }
}
