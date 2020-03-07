package svenhjol.charm.tweaks.module;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.meson.MesonModule;
import svenhjol.meson.event.ComposterEvent;
import svenhjol.meson.helper.ComposterHelper;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

import java.util.*;

@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
@Module(mod = Charm.MOD_ID, category = CharmCategories.TWEAKS, hasSubscriptions = true)
public class ComposterImprovements extends MesonModule {
    @Config(name = "Input items", description = "List of extra items that may be composted.\n" +
        "Specify the compost level chance (out of 1.0) after the item name.")
    public static List<String> inputItems = Arrays.asList("minecraft:rotten_flesh=0.3");

    @Config(name = "Output items", description = "List of extra items that can be returned from the composter.")
    public static List<String> outputItems = Arrays.asList(
        "minecraft:bone_meal",
        "minecraft:brown_mushroom",
        "minecraft:red_mushroom"
    );

    @Config(name = "Maximum output", description = "Maximum number of extra items returned from composter.")
    public static int maxOutput = 3;

    @Override
    public void onCommonSetup(FMLCommonSetupEvent event) {
        for (String input : inputItems) {
            String[] split = input.split("=");
            float chance = Float.parseFloat(split[1]);
            String itemStr = split[0];
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemStr));
            if (item != null && ComposterHelper.getChance(item) == 0F) {
                ComposterHelper.addInputItem(item, (float) Math.max(1.0, chance));
            }
        }
    }

    @SubscribeEvent
    public void onComposterOutput(ComposterEvent.Output event) {
        if (!event.getWorld().isRemote) {
            World world = event.getWorld();
            Random rand = world.rand;

            int max = Math.max(0, Math.min(maxOutput, 64));
            int num = rand.nextInt(max + 1);
            List<String> strings = new ArrayList<>(new LinkedHashSet<>(outputItems)); // get unique keys

            for (int i = 0; i < num; i++) {
                String itemStr = strings.get(rand.nextInt(strings.size()));
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemStr));
                if (item != null) {
                    ComposterHelper.spawnOutput(world, event.getPos(), new ItemStack(item));
                }
            }
        }
    }
}
