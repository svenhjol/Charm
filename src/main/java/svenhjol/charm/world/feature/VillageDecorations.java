package svenhjol.charm.world.feature;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureVillagePieces.*;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import svenhjol.charm.world.decorator.inner.VillageInnerDecorator;
import svenhjol.charm.world.decorator.outer.*;
import svenhjol.meson.Feature;
import svenhjol.meson.Meson;
import svenhjol.meson.decorator.MesonOuterDecorator;
import svenhjol.meson.event.StructureEventBase;
import svenhjol.meson.helper.WorldHelper;

import java.util.*;

public class VillageDecorations extends Feature
{
    public static boolean armorStands;
    public static boolean torches;
    public static boolean beds;
    public static boolean storage;
    public static boolean itemFrames;
    public static boolean functionalBlocks;
    public static boolean decorativeBlocks;
    public static boolean carpet;

    public static double cropsChance;
    public static double flowersChance;
    public static double lightsChance;
    public static double mobsChance;
    public static double barrelsChance;
    public static double pumpkinsChance;
    public static double treesChance;
    public static double mushroomsChance;

    public static boolean treesHaveVines;

    public static double golemsWeight;
    public static double barrelsWeight;
    public static double pumpkinsWeight;
    public static double mobsWeight;

    public static float common = 0.88f;
    public static float uncommon = 0.35f;
    public static float valuable = 0.05f;
    public static float rare = 0.005f;

    public static Map<ChunkPos, Long> villageChunks = new HashMap<>();
    public static List<ChunkPos> villageInfested = new ArrayList<>();

    @Override
    public String getDescription()
    {
        return "Improves village aesthetics with internal and external decoration.\n" +
                "Houses and huts are populated with functional and decorative blocks according to a career/profession.\n" +
                "Village area can have more trees, flowers, crops, mobs and lights.";
    }

    @Override
    public void setupConfig()
    {
        common = (float)propDouble(
                "Common chance",
                "Chance (out of 1.0) of items and blocks considered 'common' to spawn.",
                0.90
        );
        uncommon = (float)propDouble(
                "Uncommon chance",
                "Chance (out of 1.0) of items and blocks considered 'uncommon' to spawn.",
                0.35
        );
        valuable = (float)propDouble(
                "Valuable chance",
                "Chance (out of 1.0) of items and blocks considered 'valuable' to spawn.",
                0.05
        );
        rare = (float)propDouble(
                "Rare chance",
                "Chance (out of 1.0) of items and blocks considered 'rare' to spawn.",
                0.005
        );
        armorStands = propBoolean(
                "Armor Stands",
                "Armor Stands appear in village houses.",
                true
        );
        torches = propBoolean(
                "Torches",
                "Torches appear in village houses and around fields to stop water freezing.",
                true
        );
        beds = propBoolean(
                "Beds",
                "Beds appear in village houses.",
                true
        );
        storage = propBoolean(
                "Storage",
                "Chests, crates and barrels (if enabled) appear in village houses, with loot specific to the house theme.",
                true
        );
        itemFrames = propBoolean(
                "Item Frames",
                "Item frames appear on the walls of village houses, with items specific to the house theme.",
                true
        );
        functionalBlocks = propBoolean(
                "Functional Blocks",
                "Functional / Interactive blocks appear in village houses, such as anvils, cauldrons and crafting tables.",
                true
        );
        decorativeBlocks = propBoolean(
                "Decorative Blocks",
                "Decorative blocks appear in village houses, such as bookshelves, polished stone and planks.",
                true
        );
        carpet = propBoolean(
                "Carpet",
                "Coloured rugs appear in village houses and on the top of the village well.",
                true
        );
        cropsChance = propDouble(
                "Crops outside",
                "Chance (out of 1.0) of a village having tilled soil, crops and water wells within its boundary.",
                0.8
        );
        flowersChance = propDouble(
                "Flowers outside",
                "Chance (out of 1.0) of a village having more flowers (up to 3 types chosen based on village seed) within its boundary.",
                0.9
        );
        lightsChance = propDouble(
                "Lights outside",
                "Chance (out of 1.0) of a village having torches and lanterns scattered within its boundary.",
                0.8
        );
        mobsChance = propDouble(
                "Mobs outside",
                "Chance (out of 1.0) of a village having more dogs, cats and golems within its boundary.\n" +
                        "If the village is in a snowy biome, this option lets snow golems spawn.",
                0.75
        );
        barrelsChance = propDouble(
                "Barrels outside",
                "Chance (out of 1.0) of a village having barrels containing farming-related loot scattered within its boundary.",
                0.6
        );
        pumpkinsChance = propDouble(
                "Pumpkins outside",
                "Chance (out of 1.0) of a village having pumpkins (and rarely melons) scattered within its boundary.",
                0.6
        );
        treesChance = propDouble(
                "Trees outside",
                "Chance (out of 1.0) of a village having different types of trees spawn within its boundary.",
                0.75
        );
        mushroomsChance = propDouble(
                "Mushrooms outside",
                "Chance (out of 1.0) of a village having mushrooms spawn, including large mushrooms, within its boundary.",
                0.75
        );
        golemsWeight = propDouble(
                "Extra golem weight",
                "Chance (out of 1.0) of a chunk within the village boundary spawning a golem.\n" +
                        "This is only valid if the 'Mobs outside' config option allows it.",
                0.25
        );
        barrelsWeight = propDouble(
                "Extra barrels weight",
                "Chance (out of 1.0) of a chunk within the village boundary spawning a barrel.\n" +
                        "This is only valid if the 'Barrels outside' config option allows it.",
                0.7
        );
        pumpkinsWeight = propDouble(
                "Extra pumpkins weight",
                "Chance (out of 1.0) of a chunk within the village boundary spawning some pumpkins.\n" +
                        "This is only valid if the 'Pumpkins outside' config option allows it.",
                0.7
        );
        mobsWeight = propDouble(
                "Extra mobs weight",
                "Chance (out of 1.0) of a chunk allowing any kind of mob generation.\n" +
                        "The greater the chance, the more likely a mob will be considered to spawn.\n" +
                        "This is only valid is the 'Mobs outside' config option allows it.",
                0.75
        );
        treesHaveVines = propBoolean(
                "Trees can have vines",
                "If true, trees that spawn within the village boundary can have vines hanging from them.\n" +
                        "This is only valid if the 'Trees outside' config option allows it.",
                true
        );
    }

    @SubscribeEvent
    public void onPopulate(PopulateChunkEvent.Populate event)
    {
        if (event != null && event.isHasVillageGenerated()) {
            World world = event.getWorld();
            ChunkPos chunk = new ChunkPos(event.getChunkX(), event.getChunkZ());

            if (villageChunks.isEmpty() || villageChunks.get(chunk) == null) {

                // get the nearest village to the pos
                long seed = WorldHelper.getNearestVillageSeed(world, new BlockPos(chunk.x << 4, 0, chunk.z << 4));
                if (seed == 0) {
                    Meson.debug("Failed to get the seed for the nearest village...");
                    return;
                }
                villageChunks.put(chunk, seed);
            }
        }
    }

    @SubscribeEvent
    public void onDecorate(DecorateBiomeEvent.Decorate event)
    {
        if (villageChunks.get(event.getChunkPos()) != null && event.getType().equals(DecorateBiomeEvent.Decorate.EventType.FLOWERS)) {
            World world = event.getWorld();
            ChunkPos chunk = event.getChunkPos();
            BlockPos pos = new BlockPos(chunk.x << 4, 0, chunk.z << 4);
            Random eventRand = event.getRand();
            Random rand = new Random();
            long villageSeed = villageChunks.get(chunk);
            rand.setSeed(villageSeed);

            List<ChunkPos> chunks = new ArrayList<>();
            for (ChunkPos c : villageChunks.keySet()) {
                if (villageChunks.get(c) == villageSeed) chunks.add(c);
            }

            // use the rand for deterministic decoration types, eventRand for more random scattering
            List<MesonOuterDecorator> decorators = new ArrayList<>();
            if (rand.nextFloat() <= flowersChance) decorators.add(new Flowers(world, pos, rand, chunks)); // specifically rand here so we get the same flowers
            if (rand.nextFloat() <= lightsChance) decorators.add(new Lights(world, pos, eventRand, chunks));
            if (rand.nextFloat() <= mobsChance) decorators.add(new Mobs(world, pos, eventRand, chunks));
            if (rand.nextFloat() <= cropsChance) decorators.add(new Crops(world, pos, eventRand, chunks));
            if (rand.nextFloat() <= barrelsChance) decorators.add(new Barrels(world, pos, eventRand, chunks));
            if (rand.nextFloat() <= pumpkinsChance) decorators.add(new Pumpkins(world, pos, eventRand, chunks));
            if (rand.nextFloat() <= treesChance) decorators.add(new Trees(world, pos, eventRand, chunks));
            if (rand.nextFloat() <= mushroomsChance) decorators.add(new Mushrooms(world, pos, eventRand, chunks));

            decorators.forEach(MesonOuterDecorator::generate);

            villageChunks.remove(chunk);
        }
    }

    @SubscribeEvent
    public void onAddComponentParts(StructureEventBase.Post event)
    {
        if (event.getComponent() instanceof Village && !event.getWorld().isRemote) {
            World world = event.getWorld();
            StructureBoundingBox box = event.getBox();
            Village component = (Village)event.getComponent();

            VillageInnerDecorator decorator = null;

            if (component instanceof Church) decorator = new VillageInnerDecorator.Church(component, world, box);
            if (component instanceof Field2) decorator = new VillageInnerDecorator.Field1(component, world, box);
            if (component instanceof Field1) decorator = new VillageInnerDecorator.Field2(component, world, box);
            if (component instanceof Hall) decorator = new VillageInnerDecorator.Hall(component, world, box);
            if (component instanceof House1) decorator = new VillageInnerDecorator.House1(component, world, box);
            if (component instanceof House2) decorator = new VillageInnerDecorator.House2(component, world, box);
            if (component instanceof House3) decorator = new VillageInnerDecorator.House3(component, world, box);
            if (component instanceof House4Garden) decorator = new VillageInnerDecorator.House4(component, world, box);
            if (component instanceof WoodHut) decorator = new VillageInnerDecorator.WoodHut(component, world, box);
            if (component instanceof Well) decorator = new VillageInnerDecorator.Well(component, world, box);

            if (decorator != null) {
                if (decorator.isZombieInfested()) {
                    villageInfested.add(decorator.getChunkPos());
                }
                decorator.generate();
            }
        }
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
