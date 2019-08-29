package svenhjol.charm.world.feature;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import svenhjol.charm.Charm;
import svenhjol.meson.Feature;
import svenhjol.meson.Meson;
import svenhjol.meson.helper.EntityHelper;
import svenhjol.meson.helper.WorldHelper;

import static svenhjol.meson.helper.WorldHelper.*;

public class SpectreHaunting extends Feature
{
    public static StructureType structure;
    public static int distance;
    public static int checkInterval;
    public static int spawnMinDistance;
    public static int spawnRange;
    public static int ticksLiving;
    public static int mobcap;
    public static float spawnChance;

    @Override
    public String getDescription()
    {
        return "Spectres will spawn in greater numbers within range of specific structure.";
    }

    @Override
    public boolean isEnabled()
    {
        return enabled && Charm.hasFeature(Spectre.class);
    }

    @Override
    public void configure()
    {
        super.configure();

        spawnChance = (float)propDouble(
            "Spawn chance during haunt",
            "Chance (out of 1.0) of a spectre spawning every two seconds during a haunt.\n",
            0.25D
        );

        int secondsLiving = propInt(
            "Spectre lifetime",
            "How long a spectre should live for, in seconds.\n" +
                "This affects all spectres, not just those spawned in the haunt.",
            20
        );

        mobcap = propInt(
            "Check mobcap",
            "Only spawns a spectre for haunting if number of monster entities is lower than this number.\n" +
                "Vanilla minecraft is around 75.",
            80
        );

        String hauntStructure = propString(
            "Structure to haunt",
            "Name of structure that spectres will haunt.",
            StructureType.STRONGHOLD.getCapitalizedName()
        );

        checkInterval = 40; // in ticks
        distance = 7500;
        ticksLiving = secondsLiving * 20;
        spawnMinDistance = 18;
        spawnRange = 5;
        structure = StructureType.valueOf(hauntStructure.toUpperCase());
    }

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END
            && event.side.isServer()
            && event.player != null
            && event.player.world != null
            && WorldHelper.canGenerateStructures(event.player.world)
            && event.player.world.getTotalWorldTime() % checkInterval == 0
            && event.player.world.rand.nextFloat() < spawnChance
            && event.player.world.getLightBrightness(event.player.getPosition()) < ((float)Spectre.despawnLight)/16.0
        ) {
            // check spawn count
            WorldServer world = (WorldServer)event.player.world;
            EnumCreatureType monster = EnumCreatureType.MONSTER;

            int count = world.countEntities(monster, true);

            if (count <= mobcap) {
                // check structure
                BlockPos nearest = getNearestStructure(event.player.world, event.player.getPosition(), structure);
                if (nearest != null && getDistanceSq(event.player.getPosition(), nearest) < distance) {
                    Meson.debug("Spectre haunting ", event.player.getPosition());
                    EntityHelper.spawnEntityNearPlayer(event.player, spawnMinDistance, spawnRange, new ResourceLocation(Charm.MOD_ID, "spectre"));
                }
            } else {
                Meson.debug("Mobcap reached, not spawning spectre");
            }
        }
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
