package svenhjol.charm.module.lumberjacks;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.event.SetupStructureCallback.VillageType;
import svenhjol.charm.helper.VillagerHelper;
import svenhjol.charm.helper.WorldHelper;
import svenhjol.charm.init.CharmDecorations;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.module.lumberjacks.LumberjackTradeOffers.*;
import svenhjol.charm.module.woodcutters.Woodcutters;

import java.util.ArrayList;
import java.util.List;

import static svenhjol.charm.event.SetupStructureCallback.addVillageHouse;
import static svenhjol.charm.helper.VillagerHelper.addTrade;

@CommonModule(mod = Charm.MOD_ID, description = "Lumberjacks are villagers that trade wooden items. Their job site is the woodcutter.")
public class Lumberjacks extends CharmModule {
    public static String VILLAGER_ID = "charm_lumberjack";
    public static VillagerProfession LUMBERJACK;
    public static PoiType POIT;

    @Config(name = "Lumberjack house weight", description = "Chance of a custom building to spawn. For reference, a vanilla library is 5.")
    public static int buildingWeight = 5;

    @Override
    public void runWhenEnabled() {
        // TODO dedicated sounds for woodcutter and jobsite
        POIT = WorldHelper.addPointOfInterestType(Woodcutters.BLOCK_ID, Woodcutters.WOODCUTTER, 1);
        LUMBERJACK = VillagerHelper.addProfession(VILLAGER_ID, POIT, SoundEvents.VILLAGER_WORK_MASON);

        // register lumberjack trades
        addTrade(LUMBERJACK, 1, new EmeraldsForOverworldLogs());
        addTrade(LUMBERJACK, 1, new CommonSaplingsForEmeralds());
        addTrade(LUMBERJACK, 1, new LaddersForEmeralds());
        addTrade(LUMBERJACK, 2, new EmeraldsForBones());
        addTrade(LUMBERJACK, 2, new BedForEmeralds());
        addTrade(LUMBERJACK, 2, new FencesForEmeralds());
        addTrade(LUMBERJACK, 3, new BarkForLogs());
        addTrade(LUMBERJACK, 3, new EmeraldsForStems());
        addTrade(LUMBERJACK, 3, new DoorsForEmeralds());
        addTrade(LUMBERJACK, 3, new UncommonSaplingsForEmeralds());
        addTrade(LUMBERJACK, 4, new BarrelForEmeralds());
        addTrade(LUMBERJACK, 4, new MusicBlocksForLogs());
        addTrade(LUMBERJACK, 5, new BookcaseForEmeralds());
        addTrade(LUMBERJACK, 5, new WorkstationForEmeralds());

        ServerLifecycleEvents.SERVER_STARTING.register((server) -> {
            // register lumberjack structures
            StructureProcessorList vanillaProcessor = server.registryAccess().registryOrThrow(Registry.PROCESSOR_LIST_REGISTRY).get(new ResourceLocation("minecraft", "mossify_10_percent"));
            List<StructureProcessor> charmProcessor = new ArrayList<>(vanillaProcessor.list());
            charmProcessor.addAll(CharmDecorations.SINGLE_POOL_ELEMENT_PROCESSORS);

            Registry<StructureTemplatePool> poolRegistry = server.registryAccess().registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY);
            addVillageHouse(VillageType.DESERT, new ResourceLocation("charm:village/desert/houses/desert_lumberjack_1"), buildingWeight, charmProcessor, poolRegistry);
            addVillageHouse(VillageType.DESERT, new ResourceLocation("charm:village/desert/houses/desert_lumberjack_2"), buildingWeight, charmProcessor, poolRegistry);
            addVillageHouse(VillageType.PLAINS, new ResourceLocation("charm:village/plains/houses/plains_lumberjack_1"), buildingWeight, charmProcessor, poolRegistry);
            addVillageHouse(VillageType.SAVANNA, new ResourceLocation("charm:village/savanna/houses/savanna_lumberjack_1"), buildingWeight, charmProcessor, poolRegistry);
            addVillageHouse(VillageType.TAIGA, new ResourceLocation("charm:village/taiga/houses/taiga_lumberjack_1"), buildingWeight, charmProcessor, poolRegistry);
            addVillageHouse(VillageType.SNOWY, new ResourceLocation("charm:village/snowy/houses/snowy_lumberjack_2"), buildingWeight, charmProcessor, poolRegistry);
        });
    }
}
