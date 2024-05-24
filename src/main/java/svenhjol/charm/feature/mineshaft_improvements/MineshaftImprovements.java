package svenhjol.charm.feature.mineshaft_improvements;

import net.minecraft.util.Mth;
import svenhjol.charm.feature.mineshaft_improvements.common.Handlers;
import svenhjol.charm.feature.mineshaft_improvements.common.Registers;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

import java.util.List;

@Feature(description = "Adds decoration and more ores to mineshafts.")
public final class MineshaftImprovements extends CommonFeature {
    public final Registers registers;
    public final Handlers handlers;

    @Configurable(
        name = "Corridor floor blocks",
        description = "Chance (out of 1.0) of blocks such as candles and ores spawning on the floor of corridors."
    )
    private static double floorBlockChance = 0.03d;

    @Configurable(
        name = "Corridor ceiling blocks",
        description = "Chance (out of 1.0) of blocks such as lanterns spawning on the ceiling of corridors."
    )
    private static double ceilingBlockChance = 0.02d;

    @Configurable(
        name = "Corridor block piles",
        description = "Chance (out of 1.0) of stone, gravel and ore spawning at the entrance of corridors."
    )
    private static double blockPileChance = 0.2d;

    @Configurable(
        name = "Room blocks",
        description = "Chance (out of 1.0) for a moss or precious ore block to spawn on a single block of the central mineshaft room."
    )
    private static double roomBlockChance = 0.25d;

    @Configurable(
        name = "Extra minecarts",
        description = "Chance (out of 1.0) for a minecart to spawn in a corridor. Minecart loot is chosen from the 'Minecart loot' tables."
    )
    private static double minecartChance = 0.2d;

    @Configurable(
        name = "Minecart loot",
        description = "List of loot tables that will be used to populate custom minecarts."
    )
    private static List<String> minecartLoot = List.of(
        "minecraft:chests/simple_dungeon",
        "minecraft:chests/abandoned_mineshaft",
        "minecraft:chests/village/village_temple",
        "minecraft:chests/village/village_cartographer",
        "minecraft:chests/village/village_mason",
        "minecraft:chests/village/village_toolsmith",
        "minecraft:chests/village/village_weaponsmith"
    );

    public MineshaftImprovements(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
    }

    public double floorBlockChance() {
        return Mth.clamp(floorBlockChance, 0.0d, 1.0d);
    }

    public double ceilingBlockChance() {
        return Mth.clamp(ceilingBlockChance, 0.0d, 1.0d);
    }

    public double blockPileChance() {
        return Mth.clamp(blockPileChance, 0.0d, 1.0d);
    }

    public double roomBlockChance() {
        return Mth.clamp(roomBlockChance, 0.0d, 1.0d);
    }

    public double minecartChance() {
        return Mth.clamp(minecartChance, 0.0d, 1.0d);
    }

    public List<String> minecartLoot() {
        return minecartLoot;
    }
}
