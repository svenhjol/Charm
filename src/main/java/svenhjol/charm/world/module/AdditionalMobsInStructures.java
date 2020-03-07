package svenhjol.charm.world.module;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.WorldHelper.Structure;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

import java.util.*;

@Module(mod = Charm.MOD_ID, category = CharmCategories.WORLD, hasSubscriptions = true,
    description = "Allows additional hostile mobs to spawn inside world structures.\n" +
        "If allow normal mob spawns is false, only mobs specified in additional spawns will appear.")
public class AdditionalMobsInStructures extends MesonModule {
    public static Map<Structure, List<SpawnListEntry>> entries = new HashMap<>();
    public static Map<Structure, Boolean> normalSpawns = new HashMap<>();

    // desert temples
    @Config(name = "Desert pyramid additional spawns")
    public static List<String> desertPyramidAdded = new ArrayList<String>() {{
        add("husk=100");
    }};
    @Config(name = "Desert pyramids allow normal mob spawns")
    public static boolean desertPyramidNormal = false;

    // end cities
    @Config(name = "End city additional spawns")
    public static List<String> endCityAdded = new ArrayList<>();
    @Config(name = "End cities allow normal mob spawns")
    public static boolean endCityNormal = true;

    // igloos
    @Config(name = "Igloo additional spawns")
    public static List<String> iglooAdded = new ArrayList<>();
    @Config(name = "Igloos allow normal mob spawns")
    public static boolean iglooNormal = true;

    // jungle temples
    @Config(name = "Jungle temple additional spawns")
    public static List<String> jungleTempleAdded = new ArrayList<>();
    @Config(name = "Jungle temples allow normal mob spawns")
    public static boolean jungleTempleNormal = true;

    // mineshafts
    @Config(name = "Mineshaft additional spawns")
    public static List<String> mineshaftAdded = new ArrayList<>();
    @Config(name = "Mineshafts allow normal mob spawns")
    public static boolean mineshaftNormal = true;

    // nether fortresses
    @Config(name = "Nether fortress additional spawns")
    public static List<String> netherFortressAdded = new ArrayList<>();
    @Config(name = "Nether fortresses allow normal mob spawns")
    public static boolean netherFortressNormal = true;

    // ocean monuments
    @Config(name = "Ocean monument additional spawns")
    public static List<String> oceanMonumentAdded = new ArrayList<>();
    @Config(name = "Ocean monuments allow normal mob spawns")
    public static boolean oceanMonumentNormal = true;

    // ocean ruins
    @Config(name = "Ocean ruin additional spawns")
    public static List<String> oceanRuinAdded = new ArrayList<>();
    @Config(name = "Ocean ruins allow normal mob spawns")
    public static boolean oceanRuinNormal = true;

    // pillager outposts
    @Config(name = "Pillager outpost additional spawns")
    public static List<String> pillagerOutpostAdded = new ArrayList<>();
    @Config(name = "Pillager outposts allow normal mob spawns")
    public static boolean pillagerOutpostNormal = true;

    // shipwrecks
    @Config(name = "Shipwreck additional spawns")
    public static List<String> shipwreckAdded = new ArrayList<>();
    @Config(name = "Shipwrecks allow normal mob spawns")
    public static boolean shipwreckNormal = true;

    // strongholds
    @Config(name = "Stronghold additional spawns")
    public static List<String> strongholdAdded = new ArrayList<>();
    @Config(name = "Strongholds allow normal mob spawns")
    public static boolean strongholdNormal = true;

    // swamp huts
    @Config(name = "Swamp hut additional spawns")
    public static List<String> swampHutAdded = new ArrayList<>();
    @Config(name = "Swamp huts allow normal mob spawns")
    public static boolean swampHutNormal = true;

    // villages
    @Config(name = "Village additional spawns")
    public static List<String> villageAdded = new ArrayList<>();
    @Config(name = "Villages allow normal mob spawns")
    public static boolean villageNormal = true;

    // woodland mansions
    @Config(name = "Woodland mansion additional spawns")
    public static List<String> woodlandMansionAdded = new ArrayList<String>() {{
        add("vindicator=100");
    }};
    @Config(name = "Woodland mansions allow normal mob spawns")
    public static boolean woodlandMansionNormal = true;

    @Override
    public void init() {
        desertPyramidAdded.forEach(e -> configure(e, Structure.desert_pyramid, desertPyramidNormal));
        endCityAdded.forEach(e -> configure(e, Structure.endcity, endCityNormal));
        iglooAdded.forEach(e -> configure(e, Structure.igloo, iglooNormal));
        jungleTempleAdded.forEach(e -> configure(e, Structure.jungle_pyramid, jungleTempleNormal));
        mineshaftAdded.forEach(e -> configure(e, Structure.mineshaft, mineshaftNormal));
        netherFortressAdded.forEach(e -> configure(e, Structure.fortress, netherFortressNormal));
        oceanMonumentAdded.forEach(e -> configure(e, Structure.monument, oceanMonumentNormal));
        oceanRuinAdded.forEach(e -> configure(e, Structure.ocean_ruin, oceanRuinNormal));
        pillagerOutpostAdded.forEach(e -> configure(e, Structure.pillager_outpost, pillagerOutpostNormal));
        shipwreckAdded.forEach(e -> configure(e, Structure.shipwreck, shipwreckNormal));
        strongholdAdded.forEach(e -> configure(e, Structure.stronghold, strongholdNormal));
        swampHutAdded.forEach(e -> configure(e, Structure.swamp_hut, swampHutNormal));
        villageAdded.forEach(e -> configure(e, Structure.village, villageNormal));
        woodlandMansionAdded.forEach(e -> configure(e, Structure.mansion, woodlandMansionNormal));
    }

    @SubscribeEvent
    public void onPotentialSpawns(WorldEvent.PotentialSpawns event) {
        if (event.getType() != EntityClassification.MONSTER) return;

        entries.keySet().forEach(structure -> {
            net.minecraft.world.gen.feature.structure.Structure<?> featureStructure = Feature.STRUCTURES.get(structure.getName());
            if (featureStructure == null) return;
            addSpawnListEntry(event.getWorld(), event.getPos(), event.getList(), structure, featureStructure);
        });
    }

    @SuppressWarnings("deprecation")
    private void configure(String entry, Structure structure, boolean doNormal) {
        int weight = 100;
        ResourceLocation name;

        if (entry.isEmpty()) return;

        if (entry.contains("=")) {
            String[] split = entry.split("=");
            name = new ResourceLocation(split[0]);
            weight = Integer.parseInt(split[1]);
        } else {
            name = new ResourceLocation(entry);
        }

        Optional<EntityType<?>> type = Registry.ENTITY_TYPE.getValue(name);
        if (type.isPresent()) {
            if (!entries.containsKey(structure)) entries.put(structure, new ArrayList<>());
            entries.get(structure).add(new SpawnListEntry(type.get(), weight, 1, 1));
            normalSpawns.put(structure, doNormal);
        }
    }

    private void addSpawnListEntry(IWorld world, BlockPos pos, List<SpawnListEntry> list, Structure structure, net.minecraft.world.gen.feature.structure.Structure<?> featureStructure) {
        if (featureStructure.isPositionInsideStructure(world, pos)) {
            if (!normalSpawns.get(structure)) list.clear();
            list.addAll(entries.get(structure));
        }
    }
}
