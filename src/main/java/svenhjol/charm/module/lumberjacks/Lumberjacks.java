package svenhjol.charm.module.lumberjacks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.helper.VillagerHelper;
import svenhjol.charm.helper.WorldHelper;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.module.lumberjacks.LumberjackTradeOffers.*;
import svenhjol.charm.module.woodcutters.Woodcutters;
import svenhjol.charm.registry.CommonRegistry;

import static svenhjol.charm.helper.VillagerHelper.addTrade;

@CommonModule(mod = Charm.MOD_ID, description = "Lumberjacks are villagers that trade wooden items. Their job site is the woodcutter.")
public class Lumberjacks extends CharmModule {
    public static String VILLAGER_ID = "charm_lumberjack";
    public static VillagerProfession LUMBERJACK;
    public static PoiType POIT;
    public static SoundEvent VILLAGER_WORK_LUMBERJACK;

    @Override
    public void register() {
        VILLAGER_WORK_LUMBERJACK = CommonRegistry.sound(new ResourceLocation(Charm.MOD_ID, "lumberjack"));
        POIT = WorldHelper.addPointOfInterestType(Woodcutters.BLOCK_ID, Woodcutters.WOODCUTTER, 1);

        addDependencyCheck(m -> Charm.LOADER.isEnabled(Woodcutters.class));
    }

    @Override
    public void runWhenEnabled() {
        LUMBERJACK = VillagerHelper.addProfession(VILLAGER_ID, POIT, VILLAGER_WORK_LUMBERJACK);

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
        addTrade(LUMBERJACK, 5, new WorkstationForEmeralds());
    }
}
