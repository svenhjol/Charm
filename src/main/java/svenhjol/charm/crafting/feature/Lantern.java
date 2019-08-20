package svenhjol.charm.crafting.feature;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import svenhjol.charm.crafting.block.BlockLantern;
import svenhjol.meson.Feature;
import svenhjol.meson.handler.RecipeHandler;
import svenhjol.meson.helper.ForgeHelper;
import svenhjol.meson.registry.ProxyRegistry;

public class Lantern extends Feature
{
    public static BlockLantern ironLantern, goldLantern;
    public static float hardness;
    public static float resistance;
    public static float lightLevel;
    public static int numberOfLanterns;
    public static boolean falling;
    public static boolean playSound;
    public static boolean useCharmLanterns;

    @Override
    public String getDescription()
    {
        return "An elegant lighting solution.  Comes in iron and gold.";
    }

    @Override
    public void configure()
    {
        super.configure();

        falling = propBoolean(
            "Lanterns obey gravity",
            "Lantern will fall when the block under it is broken, or when the block above a hanging lantern is broken.",
            true
        );

        useCharmLanterns = propBoolean(
            "Use Charm lanterns",
            "Charm's lanterns will be enabled even if lanterns from other mods are present.",
            false
        );

        numberOfLanterns = propInt(
            "Number of lanterns crafted",
            "Number of lanterns output when crafting a lantern.",
            1
        );

        // internal
        hardness = 3.5f;
        resistance = 4.0f;
        lightLevel = 1.0f;
        playSound = true;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);
        if (useCharmLanterns || !ForgeHelper.areModsLoaded("minecraftfuture")) {
            // register iron lantern if not overridden by other mods
            ironLantern = new BlockLantern("iron");
            RecipeHandler.addShapedRecipe(ProxyRegistry.newStack(ironLantern, numberOfLanterns),
                "III", "ITI", "III",
                'I', Items.IRON_NUGGET,
                'T', Blocks.TORCH
            );
        }

        goldLantern = new BlockLantern("gold");
        RecipeHandler.addShapedRecipe(ProxyRegistry.newStack(goldLantern, numberOfLanterns),
            "GGG", "GTG", "GGG",
            'G', Items.GOLD_NUGGET,
            'T', Blocks.TORCH
        );
    }

    public static BlockLantern getDefaultLantern()
    {
        return ironLantern == null ? goldLantern : ironLantern;
    }
}
