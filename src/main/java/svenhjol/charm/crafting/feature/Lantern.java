package svenhjol.charm.crafting.feature;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import svenhjol.charm.crafting.block.BlockLantern;
import svenhjol.meson.Feature;
import svenhjol.meson.ProxyRegistry;
import svenhjol.meson.RecipeHandler;
import svenhjol.meson.helper.ConfigHelper;

public class Lantern extends Feature
{
    public static BlockLantern ironLantern, goldLantern;
    public static float hardness;
    public static float resistance;
    public static float lightLevel;
    public static boolean falling;
    public static boolean playSound;

    @Override
    public String getDescription()
    {
        return "An elegant lighting solution borrowed lovingly from Minecraft 1.14.\n" +
                "Now with special Golden edition!";
    }

    @Override
    public void setupConfig()
    {
        falling = propBoolean(
            "Lanterns obey gravity",
            "Lantern will fall when the block under it is broken, or when the block above a hanging lantern is broken.",
            true
        );

        playSound = propBoolean(
            "Ambient sound",
            "Lanterns make a gentle fire crackling sound.",
            true
        );

        // internal
        hardness = 2.0f;
        resistance = 4.0f;
        lightLevel = 1.0f;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        if (!ConfigHelper.checkMods("minecraftfuture")) {
            // register iron lantern if not overridden by other mods
            ironLantern = new BlockLantern("iron");
            RecipeHandler.addShapedRecipe(ProxyRegistry.newStack(ironLantern, 2),
                "III", "ITI", "III",
                'I', Items.IRON_NUGGET,
                'T', Blocks.TORCH
            );
        }

        goldLantern = new BlockLantern("gold");
        RecipeHandler.addShapedRecipe(ProxyRegistry.newStack(goldLantern, 2),
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