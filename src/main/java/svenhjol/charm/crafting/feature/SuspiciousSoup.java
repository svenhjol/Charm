package svenhjol.charm.crafting.feature;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetMetadata;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import svenhjol.charm.base.CharmLootTables;
import svenhjol.charm.crafting.item.ItemSuspiciousSoup;
import svenhjol.charm.crafting.potion.SuspiciousEffects;
import svenhjol.meson.Feature;
import svenhjol.meson.ProxyRegistry;
import svenhjol.meson.RecipeHandler;
import svenhjol.meson.helper.LootHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SuspiciousSoup extends Feature
{
    public static Item suspiciousSoup;
    public static List<SuspiciousEffects.CustomEffect> effects = new ArrayList<>();
    public static long lastSeed;

    public static int duration; // how long the suspicious soup effects last
    public static int amplifier; // effect strength
    public static int heal; // how many points the soup heals you
    public static double saturation; // how much saturation you get from the soup
    public static boolean addToLoot;

    public static int maxTypes;
    public static int maxStackSize;

    @Override
    public String getDescription()
    {
        return "Craft (or find) beetroot soup with a flower to make a suspicious soup.\n" +
                "Different flowers provide positive effects that are different for each world.\n" +
                "Be warned: these unstable concoctions might occasionally have negative side effects.";
    }

    @Override
    public void setupConfig()
    {
        // configurable
        duration = propInt(
                "Suspicious Soup duration",
                "Minimum duration (in seconds) of the effect(s) of the soup when consumed.",
                20
        );
        heal = propInt(
                "Health restored",
                "Amount of half-hearts of health restored by consuming any soup.",
                10
        );
        addToLoot = propBoolean(
                "Add to loot",
                "Add soup to mansions, igloos and village blacksmith loot.",
                true
        );

        // internal
        SuspiciousEffects.init();
        maxTypes = effects.size(); // should match the number of suspicious effects
        maxStackSize = 16;
        amplifier = 1;
        saturation = 0.2D;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        suspiciousSoup = new ItemSuspiciousSoup();

        // dynamic flower recipes
        List<ItemStack> flowers = new ArrayList<>();
        flowers.add(new ItemStack(Blocks.YELLOW_FLOWER, 1));
        for (int i = 0; i <= 8; i++) {
            flowers.add(new ItemStack(Blocks.RED_FLOWER, 1, i));
        }
        for (int i = 0; i <= 5; i++) {
            flowers.add(new ItemStack(Blocks.DOUBLE_PLANT, 1, i));
        }

        int i = 0;
        for (ItemStack flower : flowers) {
            RecipeHandler.addShapedRecipe(ProxyRegistry.newStack(suspiciousSoup, 1, i),
                "BBB", "BBB", "FWF",
                'B', Items.BEETROOT,
                'W', Items.BOWL,
                'F', flower
            );
            if (++i == maxTypes) i = 0;
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event)
    {
        World world = event.getWorld();
        if (world.isRemote) return;

        if (effects.isEmpty() || lastSeed != world.getSeed()) {
            lastSeed = world.getSeed();
            assignEffects();
        }
    }

    @SubscribeEvent
    public void onLootTableLoad(LootTableLoadEvent event)
    {
        if (!addToLoot) return;
        int weight = 0;
        int quality = 0;

        LootCondition[] conditions = new LootCondition[0];
        LootFunction[] functions = new LootFunction[] { new SetMetadata(new LootCondition[0], new RandomValueRange(0, maxTypes-1)) };

        if (event.getName().equals(LootTableList.CHESTS_WOODLAND_MANSION)) { weight = 14; }
        if (event.getName().equals(LootTableList.CHESTS_IGLOO_CHEST)) { weight = 14; }
        if (event.getName().equals(LootTableList.CHESTS_VILLAGE_BLACKSMITH)) { weight = 14; }
        if (event.getName().equals(CharmLootTables.VILLAGE_FARMER)) { weight = 14; }
        if (event.getName().equals(CharmLootTables.VILLAGE_BUTCHER)) { weight = 14; }

        if (weight > 0) {
            LootHelper.addToLootTable(event.getTable(), suspiciousSoup, weight, quality, functions, conditions);
        }
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }

    private void assignEffects()
    {
        Collections.shuffle(effects, new Random(lastSeed));
    }
}
