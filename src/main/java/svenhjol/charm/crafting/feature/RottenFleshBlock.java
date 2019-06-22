package svenhjol.charm.crafting.feature;

import net.minecraft.init.Items;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import svenhjol.charm.crafting.block.BlockRottenFlesh;
import svenhjol.meson.Feature;
import svenhjol.meson.registry.ProxyRegistry;
import svenhjol.meson.handler.RecipeHandler;

public class RottenFleshBlock extends Feature
{
    public static float hardness;
    public static float resistance;
    public static int harvestLevel;
    public static boolean showParticles;
    public static boolean transformToPodzol;
    public static boolean transformToSoil;

    public static BlockRottenFlesh block;

    @Override
    public String getDescription()
    {
        return "A storage block for rotten flesh.  If the rotten flesh block has water on any of its sides, it has a chance to turn into dirt.\n" +
                "If there is a soil block above the rotten flesh block, it has a chance to turn into podzol.";
    }

    @Override
    public void setupConfig()
    {
        transformToSoil = propBoolean(
                "Transform to soil",
                "If true, this block will turn to dirt over time when surrounded by water on at least one side.",
                true
        );
        transformToPodzol = propBoolean(
                "Transform soil above to podzol",
                "If true, a soil block directly above this rotten flesh block will turn to podzol over time.",
                true
        );
        showParticles = propBoolean(
                "Show particles",
                "Flesh blocks give off random particles like mycelium.",
                true
        );

        // internal
        hardness = 1.0f;
        resistance = 2.0f;
        harvestLevel = 1;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        block = new BlockRottenFlesh();
        RecipeHandler.addShapedRecipe(ProxyRegistry.newStack(block, 1),
            "RRR", "RRR", "RRR",
            'R', Items.ROTTEN_FLESH
        );
        RecipeHandler.addShapelessRecipe(ProxyRegistry.newStack(Items.ROTTEN_FLESH, 9), ProxyRegistry.newStack(block));
    }
}
