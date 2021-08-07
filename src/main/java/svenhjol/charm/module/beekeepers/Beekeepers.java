package svenhjol.charm.module.beekeepers;

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
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.event.SetupStructureCallback.VillageType;
import svenhjol.charm.helper.VillagerHelper;
import svenhjol.charm.init.CharmDecorations;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.mixin.accessor.PoiTypeAccessor;

import java.util.ArrayList;
import java.util.List;

import static svenhjol.charm.event.SetupStructureCallback.addVillageHouse;
import static svenhjol.charm.helper.VillagerHelper.addTrade;

@CommonModule(mod = Charm.MOD_ID, description = "Beekeepers are villagers that trade beekeeping items. Their job site is the beehive.")
public class Beekeepers extends CharmModule {
    public static String ID = "charm_beekeeper";
    public static VillagerProfession BEEKEEPER;

    @Config(name = "Beekeeper house weight", description = "Chance of a custom building to spawn. For reference, a vanilla library is 5.")
    public static int buildingWeight = 5;

    @Override
    public void runWhenEnabled() {
        BEEKEEPER = VillagerHelper.addProfession(ID, PoiType.BEEHIVE, SoundEvents.BEEHIVE_WORK);

        // HACK: set ticketCount so that villager can use it as job site
        ((PoiTypeAccessor)PoiType.BEEHIVE).setMaxTickets(1);

        // register beekeeper trades
        addTrade(BEEKEEPER, 1, new BeekeeperTradeOffers.EmeraldsForFlowers());
        addTrade(BEEKEEPER, 1, new BeekeeperTradeOffers.BottlesForEmerald());
        addTrade(BEEKEEPER, 2, new BeekeeperTradeOffers.EmeraldsForCharcoal());
        addTrade(BEEKEEPER, 2, new BeekeeperTradeOffers.CandlesForEmeralds());
        addTrade(BEEKEEPER, 3, new BeekeeperTradeOffers.EmeraldsForHoneycomb());
        addTrade(BEEKEEPER, 3, new BeekeeperTradeOffers.CampfireForEmerald());
        addTrade(BEEKEEPER, 4, new BeekeeperTradeOffers.LeadForEmeralds());
        addTrade(BEEKEEPER, 5, new BeekeeperTradeOffers.PopulatedBeehiveForEmeralds());

        // register beekeeper structures
        ServerLifecycleEvents.SERVER_STARTING.register((server) -> {
            StructureProcessorList vanillaProcessor = server.registryAccess().registryOrThrow(Registry.PROCESSOR_LIST_REGISTRY).get(new ResourceLocation("minecraft", "mossify_10_percent"));
            List<StructureProcessor> charmProcessor = new ArrayList<>(vanillaProcessor.list());
            charmProcessor.addAll(CharmDecorations.SINGLE_POOL_ELEMENT_PROCESSORS);

            Registry<StructureTemplatePool> poolRegistry = server.registryAccess().registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY);
            addVillageHouse(VillageType.PLAINS, new ResourceLocation("charm:village/plains/houses/plains_beejack"), buildingWeight, charmProcessor, poolRegistry);
            addVillageHouse(VillageType.PLAINS, new ResourceLocation("charm:village/plains/houses/plains_beekeeper_1"), buildingWeight, charmProcessor, poolRegistry);
            addVillageHouse(VillageType.DESERT, new ResourceLocation("charm:village/desert/houses/desert_beekeeper_1"), buildingWeight, charmProcessor, poolRegistry);
            addVillageHouse(VillageType.SAVANNA, new ResourceLocation("charm:village/savanna/houses/savanna_beekeeper_1"), buildingWeight, charmProcessor, poolRegistry);
            addVillageHouse(VillageType.SAVANNA, new ResourceLocation("charm:village/savanna/houses/savanna_beekeeper_2"), buildingWeight, charmProcessor, poolRegistry);
            addVillageHouse(VillageType.TAIGA, new ResourceLocation("charm:village/taiga/houses/taiga_beekeeper_1"), buildingWeight, charmProcessor, poolRegistry);
            addVillageHouse(VillageType.SNOWY, new ResourceLocation("charm:village/snowy/houses/snowy_lumberbee_1"), buildingWeight, charmProcessor, poolRegistry);
        });
    }
}
