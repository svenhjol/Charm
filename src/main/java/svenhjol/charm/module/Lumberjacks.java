package svenhjol.charm.module;

import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.helper.VillagerHelper;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.event.StructureSetupCallback.VillageType;
import svenhjol.charm.village.LumberjackTradeOffers.*;

import static svenhjol.charm.base.helper.VillagerHelper.addTrade;
import static svenhjol.charm.event.StructureSetupCallback.addVillageHouse;

@Module(mod = Charm.MOD_ID, description = "Lumberjacks are villagers that trade wooden items. Their job site is the woodcutter.")
public class Lumberjacks extends CharmModule {
    public static Identifier VILLAGER_ID = new Identifier(Charm.MOD_ID, "lumberjack");
    public static VillagerProfession LUMBERJACK;
    public static PointOfInterestType POIT;

    @Config(name = "Lumberjack house weight", description = "Chance of a custom building to spawn. For reference, a vanilla library is 5.")
    public static int buildingWeight = 5;

    @Override
    public void init() {
        // TODO dedicated sounds for woodcutter and jobsite
        POIT = VillagerHelper.addPointOfInterestType(Woodcutters.BLOCK_ID, Woodcutters.WOODCUTTER, 1);
        LUMBERJACK = VillagerHelper.addProfession(VILLAGER_ID, POIT, SoundEvents.ENTITY_VILLAGER_WORK_MASON);

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

        // register lumberjack structures
        addVillageHouse(VillageType.DESERT, new Identifier("charm:village/desert/houses/desert_lumberjack_1"), buildingWeight);
        addVillageHouse(VillageType.DESERT, new Identifier("charm:village/desert/houses/desert_lumberjack_2"), buildingWeight);
        addVillageHouse(VillageType.PLAINS, new Identifier("charm:village/plains/houses/plains_lumberjack_1"), buildingWeight);
        addVillageHouse(VillageType.SAVANNA, new Identifier("charm:village/savanna/houses/savanna_lumberjack_1"), buildingWeight);
        addVillageHouse(VillageType.TAIGA, new Identifier("charm:village/taiga/houses/taiga_lumberjack_1"), buildingWeight);
        addVillageHouse(VillageType.SNOWY, new Identifier("charm:village/snowy/houses/snowy_lumberjack_2"), buildingWeight);
    }
}
