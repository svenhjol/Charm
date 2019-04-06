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

    public static float common = 0.85f;
    public static float uncommon = 0.4f;
    public static float valuable = 0.05f;
    public static float rare = 0.005f;

    public static Map<ChunkPos, Long> villageChunks = new HashMap<>();

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
                0.8
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

            Random rand = new Random();
            long villageSeed = villageChunks.get(chunk);
            rand.setSeed(villageSeed);
//            Meson.debug("Decorate", chunk, villageSeed);

            List<ChunkPos> chunks = new ArrayList<>();
            for (ChunkPos c : villageChunks.keySet()) {
                if (villageChunks.get(c) == villageSeed) chunks.add(c);
            }

            // use the village seed rand for deterministic decoration types
            List<MesonOuterDecorator> decorators = new ArrayList<>();
            if (rand.nextFloat() <= 0.9f) decorators.add(new Flowers(world, pos, rand, chunks));
            if (rand.nextFloat() <= 1.0f) decorators.add(new Lights(world, pos, rand, chunks));
            if (rand.nextFloat() <= 0.9f) decorators.add(new Mobs(world, pos, rand, chunks));
            if (rand.nextFloat() <= 0.7f) decorators.add(new Crops(world, pos, rand, chunks));
            if (rand.nextFloat() <= 0.7f) decorators.add(new Barrels(world, pos, rand, chunks));
            if (rand.nextFloat() <= 0.7f) decorators.add(new Trees(world, pos, rand, chunks));
            if (rand.nextFloat() <= 0.7f) decorators.add(new Mushrooms(world, pos, rand, chunks));

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
