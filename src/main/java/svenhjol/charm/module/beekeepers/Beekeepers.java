package svenhjol.charm.module.beekeepers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.event.SetupStructureCallback.VillageType;
import svenhjol.charm.helper.VillagerHelper;
import svenhjol.charm.loader.CharmCommonModule;
import svenhjol.charm.mixin.accessor.PoiTypeAccessor;

import static svenhjol.charm.event.SetupStructureCallback.addVillageHouse;
import static svenhjol.charm.helper.VillagerHelper.addTrade;

@CommonModule(mod = Charm.MOD_ID, description = "Beekeepers are villagers that trade beekeeping items. Their job site is the beehive.")
public class Beekeepers extends CharmCommonModule {
    public static String ID = "charm_beekeeper";
    public static VillagerProfession BEEKEEPER;

    @Config(name = "Beekeeper house weight", description = "Chance of a custom building to spawn. For reference, a vanilla library is 5.")
    public static int buildingWeight = 5;

    @Override
    public void run() {
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
        addVillageHouse(VillageType.PLAINS, new ResourceLocation("charm:village/plains/houses/plains_beejack"), buildingWeight);
        addVillageHouse(VillageType.PLAINS, new ResourceLocation("charm:village/plains/houses/plains_beekeeper_1"), buildingWeight);
        addVillageHouse(VillageType.DESERT, new ResourceLocation("charm:village/desert/houses/desert_beekeeper_1"), buildingWeight);
        addVillageHouse(VillageType.SAVANNA, new ResourceLocation("charm:village/savanna/houses/savanna_beekeeper_1"), buildingWeight);
        addVillageHouse(VillageType.SAVANNA, new ResourceLocation("charm:village/savanna/houses/savanna_beekeeper_2"), buildingWeight);
        addVillageHouse(VillageType.TAIGA, new ResourceLocation("charm:village/taiga/houses/taiga_beekeeper_1"), buildingWeight);
        addVillageHouse(VillageType.SNOWY, new ResourceLocation("charm:village/snowy/houses/snowy_lumberbee_1"), buildingWeight);
    }
}
