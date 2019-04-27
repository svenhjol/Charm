package svenhjol.charm.crafting.feature;

import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.init.Items;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import svenhjol.charm.crafting.ai.AIFormEndermite;
import svenhjol.charm.crafting.block.BlockEnderPearl;
import svenhjol.meson.Feature;
import svenhjol.meson.Meson;
import svenhjol.meson.ProxyRegistry;
import svenhjol.meson.RecipeHandler;

public class EnderPearlBlock extends Feature
{
    public static BlockEnderPearl block;
    public static int hardness;
    public static boolean showParticles;
    public static double endermiteChance;

    @Override
    public void setupConfig()
    {
        endermiteChance = propDouble(
                "Silverfish burrowing",
                "Chance (out of 1.0) of a silverfish burrowing into an Ender Pearl Block, creating an Endermite.",
                0.5D
        );

        // internal
        hardness = 2;
        showParticles = true;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        block = new BlockEnderPearl();

        RecipeHandler.addShapedRecipe(ProxyRegistry.newStack(block, 1),
                "EEE", "EEE", "EEE",
                'E', Items.ENDER_PEARL
        );
        RecipeHandler.addShapelessRecipe(ProxyRegistry.newStack(Items.ENDER_PEARL, 9),
                ProxyRegistry.newStack(block));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onItemUseFinish(LivingEntityUseItemEvent.Finish event)
    {
        if (event.getEntityLiving() != null
            && !event.getEntityLiving().world.isRemote
            && event.getItem().getItem() == Items.CHORUS_FRUIT
        ) {
            Meson.log("Nom");
            event.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent
    public void onEnterChunk(EntityEvent.EnteringChunk event)
    {
        if (event.getEntity() instanceof EntitySilverfish) {
            EntitySilverfish silverfish = (EntitySilverfish) event.getEntity();
            for (EntityAITasks.EntityAITaskEntry task : silverfish.tasks.taskEntries) {
                if (task.action instanceof AIFormEndermite) return;
            }
            silverfish.tasks.addTask(2, new AIFormEndermite(silverfish));
        }
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
