package svenhjol.charm.loot.feature;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.crafting.feature.Crate;
import svenhjol.meson.Feature;
import svenhjol.meson.Meson;

import java.util.Random;

public class AbandonedCrates extends Feature
{
    public static int maxTries;
    public static int maxDepth;
    public static int startDepth;
    public static double generateChance;
    public static float rareChance;
    public static float valuableChance;
    public static float uncommonChance;

    @Override
    public String getDescription()
    {
        return "Abandoned Crates may be found underground.  These crates are sealed so must be smashed to obtain contained items.";
    }

    @Override
    public void setupConfig()
    {
        generateChance = propDouble(
            "Generate crate chance",
            "Chance (out of 1.0) of a crate generating in a chunk, if it is possible to do so.",
            0.18D
        );

        // internal
        startDepth = 16;
        maxDepth = 32;
        maxTries = 1;
        rareChance = 0.005f;
        valuableChance = 0.08f;
        uncommonChance = 0.2f;
    }

    @SubscribeEvent
    public void onPopulate(PopulateChunkEvent.Populate event)
    {
        if (!Charm.hasFeature(Crate.class)) return;

        if (event != null
            && !event.getWorld().isRemote
            && event.getWorld().provider.getDimension() == 0
            && event.getRand().nextFloat() <= generateChance
        ) {
            Random rand = event.getRand();
            generateCrate(event.getWorld(), new ChunkPos(event.getChunkX(), event.getChunkZ()), event.getRand(), startDepth + rand.nextInt(20), maxDepth);
        }
    }

    public static void generateCrate(World world, ChunkPos chunkPos, Random rand, int start, int max)
    {
        int tries = 0;
        boolean valid = false;
        BlockPos cratePos;

        do {
            if (tries > maxTries) return;

            int xx = rand.nextInt(16) + 8;
            int zz = rand.nextInt(16) + 8;
            BlockPos pos = new BlockPos((chunkPos.x << 4) + xx, 255, (chunkPos.z << 4) + zz);

            cratePos = world.getTopSolidOrLiquidBlock(pos).add(0, -start, 0);
            for (int d = 0; d < max; d += 2) {
                cratePos.add(0, -1, 0);
                IBlockState state = world.getBlockState(cratePos);
                if (state.getMaterial() == Material.AIR) {
                    Material below = world.getBlockState(cratePos.down()).getMaterial();
                    if (below == Material.ROCK || below == Material.WOOD) {
                        valid = true;
                        break;
                    }
                }
            }
            tries++;
        } while (!valid);

        float f = world.rand.nextFloat();
        Crate.RARITY rarity = Crate.RARITY.COMMON;
        if (f <= rareChance) {
            rarity = Crate.RARITY.RARE;
        } else if (f <= valuableChance) {
            rarity = Crate.RARITY.VALUABLE;
        } else if (f <= uncommonChance) {
            rarity = Crate.RARITY.RARE;
        }

        Crate.generateCrate(world, cratePos, Crate.getRandomCrateType(rarity), true);
        Meson.debug("Abandoned Crates: generated crate", cratePos);
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }

    @Override
    public boolean hasTerrainSubscriptions()
    {
        return true;
    }
}
