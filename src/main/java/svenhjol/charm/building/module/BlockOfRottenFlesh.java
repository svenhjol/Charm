package svenhjol.charm.building.module;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.building.block.RottenFleshBlock;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.ComposterHelper;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

@Module(mod = Charm.MOD_ID, category = CharmCategories.BUILDING,
    description = "A storage block for rotten flesh.  If the rotten flesh block has water on any of its sides, it has a chance to turn into dirt.\n" +
        "If there is a soil block above the rotten flesh block, it has a chance to turn into podzol.")
public class BlockOfRottenFlesh extends MesonModule {
    public static RottenFleshBlock block;
    private static final int DEFAULT = 7;
    private static int cachedGrowChance = -1;

    @Config(name = "Compostable", description = "If true, a block of rotten flesh can be composted.")
    public static boolean compostable = true;

    @Config(name = "GrowChance", description = "Chance (out of 10) that the crop planted above a block of rotten flesh WILL NOT get a growth boost each tick. E.g., enter a 6 means that the crop has a 4 out of 10 chance of getting a growth boost each tick. (Default: 7)")
    public static int growChance = 7;

    public static int getGrowChance() {
        if (cachedGrowChance == -1) {
            cachedGrowChance = Meson.isModuleEnabled("charm:block_of_rotten_flesh") ? growChance : DEFAULT;
        }
        return cachedGrowChance;
    }

    @Override
    public void init() {
        block = new RottenFleshBlock(this);
    }

    @Override
    public void onCommonSetup(FMLCommonSetupEvent event) {
        if (compostable) ComposterHelper.addInputItem(new ItemStack(block).getItem(), 1.0F);
    }
}